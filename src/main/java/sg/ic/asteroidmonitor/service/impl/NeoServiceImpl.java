package sg.ic.asteroidmonitor.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import sg.ic.asteroidmonitor.dto.FeedDateRangeReq;
import sg.ic.asteroidmonitor.dto.NeoFeedResponse;
import sg.ic.asteroidmonitor.dto.NeoLookupResponse;
import sg.ic.asteroidmonitor.dto.StandardFeedResponse;
import sg.ic.asteroidmonitor.dto.StandardLookupResponse;
import sg.ic.asteroidmonitor.dto.StandardFeedResponse.Diameter;
import sg.ic.asteroidmonitor.dto.StandardLookupResponse.ApproachData;
import sg.ic.asteroidmonitor.dto.NeoFeedResponse.CloseApproachData;
import sg.ic.asteroidmonitor.dto.NeoFeedResponse.NearEarthObject;
import sg.ic.asteroidmonitor.feign.NasaNeoClient;
import sg.ic.asteroidmonitor.service.NeoService;

@Service
@Slf4j
public class NeoServiceImpl implements NeoService {

    private final NasaNeoClient nasaNeoClient;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public NeoServiceImpl(NasaNeoClient nasaNeoClient) {
        this.nasaNeoClient = nasaNeoClient;
    }

    @Override
    public List<StandardFeedResponse> getFeed(FeedDateRangeReq request) {
        NeoFeedResponse apiRes = nasaNeoClient.getNeoFeed(request.getStartDate().format(FORMATTER),
                request.getEndDate().format(FORMATTER), "DEMO_KEY");
        List<NearEarthObject> neo = apiRes.getNearEarthObjects().values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return getTop10Closest(neo);
    }

    @Override
    public StandardLookupResponse getLookUp(String id) {
        return responseToStandardLookupDto(nasaNeoClient.getLookUp(id, "DEMO_KEY"));
    }

    private List<StandardFeedResponse> getTop10Closest(List<NearEarthObject> responses) {
        var topTenAsteroids = responses.stream()
                .flatMap(neo -> neo.getCloseApproachData().stream())
                .filter(cad -> cad.getMissDistance() != null && cad.getMissDistance().getKilometers() != null)
                .sorted(Comparator.comparing(cad -> Double.parseDouble(cad.getMissDistance().getKilometers())))
                .limit(10)
                .map(cad -> findObjectByApproach(responses, cad))
                .distinct()
                .collect(Collectors.toList());

        return responseToStandardFeedDto(topTenAsteroids);
    }

    private NearEarthObject findObjectByApproach(List<NearEarthObject> responses, CloseApproachData cad) {
        return responses.stream()
                .filter(neo -> neo.getCloseApproachData().contains(cad))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find NearEarthObject for CloseApproachData"));
    }

    private List<StandardFeedResponse> responseToStandardFeedDto(List<NearEarthObject> responses) {
        return responses.stream()
                .map(i -> StandardFeedResponse.builder()
                        .id(i.getId())
                        .name(i.getName())
                        .approachDate(i.getCloseApproachData().get(0).getCloseApproachDateFull())
                        .distanceFromEarthKm(i.getCloseApproachData().get(0).getMissDistance().getKilometers())
                        .relativeVelocityKph(
                                i.getCloseApproachData().get(0).getRelativeVelocity().getKilometersPerHour())
                        .isHazardous(i.isPotentiallyHazardousAsteroid())
                        .diameter(new Diameter(i.getEstimatedDiameter().getKilometers().getEstimatedDiameterMin(),
                                i.getEstimatedDiameter().getKilometers().getEstimatedDiameterMax()))
                        .build())
                .collect(Collectors.toList());
    }

    private StandardLookupResponse responseToStandardLookupDto(NeoLookupResponse response) {
        List<ApproachData> appr = new ArrayList<>();
        for (var el : response.getCloseApproachData()) {
            appr.add(new ApproachData(el.getCloseApproachDateFull(), el.getMissDistance().getKilometers(),
                    el.getRelativeVelocity().getKilometersPerHour()));
        }
        return StandardLookupResponse.builder()
                .id(response.getId())
                .name(response.getName())
                .diameter(new Diameter(response.getEstimatedDiameter().getKilometers().getEstimatedDiameterMin(),
                        response.getEstimatedDiameter().getKilometers().getEstimatedDiameterMax()))
                .isHazardous(response.isPotentiallyHazardousAsteroid())
                .approachData(appr)
                .build();
    }
}
