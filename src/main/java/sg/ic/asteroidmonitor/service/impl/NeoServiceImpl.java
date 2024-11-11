package sg.ic.asteroidmonitor.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.ic.asteroidmonitor.dto.FeedDateRangeReq;
import sg.ic.asteroidmonitor.dto.NeoFeedResponse;
import sg.ic.asteroidmonitor.dto.NeoFeedResponse.CloseApproachData;
import sg.ic.asteroidmonitor.dto.NeoFeedResponse.NearEarthObject;
import sg.ic.asteroidmonitor.feign.NasaNeoClient;
import sg.ic.asteroidmonitor.service.NeoService;

@Service
public class NeoServiceImpl implements NeoService {

    private final NasaNeoClient nasaNeoClient;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public NeoServiceImpl(NasaNeoClient nasaNeoClient) {
        this.nasaNeoClient = nasaNeoClient;
    }

    @Override
    public List<NearEarthObject> getFeed(FeedDateRangeReq request) {
        NeoFeedResponse apiRes = nasaNeoClient.getNeoFeed(request.getStartDate().format(FORMATTER),
                request.getEndDate().format(FORMATTER), "DEMO_KEY");
        List<NearEarthObject> neo = apiRes.getNearEarthObjects().values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return getTop10Closest(neo);
    }

    @Override
    public Map<String, Object> getLookUp(String id) {
        return nasaNeoClient.getLookUp(id, "DEMO_KEY");
    }

    private List<NearEarthObject> getTop10Closest(List<NearEarthObject> responses) {
        return responses.stream()
                .flatMap(neo -> neo.getCloseApproachData().stream())
                .filter(cad -> cad.getMissDistance() != null && cad.getMissDistance().getKilometers() != null)
                .sorted(Comparator.comparing(cad -> Double.parseDouble(cad.getMissDistance().getKilometers())))
                .limit(10)
                .map(cad -> findObjectByApproach(responses, cad))
                .distinct()
                .collect(Collectors.toList());
    }

    private NearEarthObject findObjectByApproach(List<NearEarthObject> responses, CloseApproachData cad) {
        return responses.stream()
                .filter(neo -> neo.getCloseApproachData().contains(cad))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find NearEarthObject for CloseApproachData"));
    }
}
