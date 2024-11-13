package sg.ic.asteroidmonitor.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import sg.ic.asteroidmonitor.dto.NeoFeedResponse.CloseApproachData;
import sg.ic.asteroidmonitor.dto.NeoFeedResponse.NearEarthObject;
import sg.ic.asteroidmonitor.dto.NeoLookupResponse;
import sg.ic.asteroidmonitor.dto.StandardFeedResponse;
import sg.ic.asteroidmonitor.dto.StandardFeedResponse.Diameter;
import sg.ic.asteroidmonitor.dto.StandardLookupResponse;
import sg.ic.asteroidmonitor.dto.StandardLookupResponse.ApproachData;

public class FilterResponseUtil {

        private FilterResponseUtil() {
        }

        public static List<StandardFeedResponse> getTop10Closest(List<NearEarthObject> responses) {
                var topTenAsteroids = responses.stream()
                                .flatMap(neo -> neo.getCloseApproachData().stream())
                                .filter(cad -> cad.getMissDistance() != null
                                                && cad.getMissDistance().getKilometers() != null)
                                .sorted(Comparator.comparing(
                                                cad -> Double.parseDouble(cad.getMissDistance().getKilometers())))
                                .limit(10)
                                .map(cad -> findObjectByApproach(responses, cad))
                                .distinct()
                                .collect(Collectors.toList());

                return responseToStandardFeedDto(topTenAsteroids);
        }

        public static List<NearEarthObject> getTop10ClosestInYear(List<NearEarthObject> responses) {
                return responses.stream()
                                .flatMap(neo -> neo.getCloseApproachData().stream())
                                .filter(cad -> cad.getMissDistance() != null
                                                && cad.getMissDistance().getKilometers() != null)
                                .sorted(Comparator.comparing(
                                                cad -> Double.parseDouble(cad.getMissDistance().getKilometers())))
                                .limit(10)
                                .map(cad -> findObjectByApproach(responses, cad))
                                .distinct()
                                .collect(Collectors.toList());
        }

        private static NearEarthObject findObjectByApproach(List<NearEarthObject> responses, CloseApproachData cad) {
                return responses.stream()
                                .filter(neo -> neo.getCloseApproachData().contains(cad))
                                .findFirst()
                                .orElseThrow(() -> new IllegalStateException(
                                                "Could not find NearEarthObject for CloseApproachData"));
        }

        public static List<StandardFeedResponse> responseToStandardFeedDto(List<NearEarthObject> responses) {
                return responses.stream()
                                .map(i -> StandardFeedResponse.builder()
                                                .id(i.getId())
                                                .name(i.getName())
                                                .approachDate(i.getCloseApproachData().get(0)
                                                                .getCloseApproachDateFull())
                                                .distanceFromEarthKm(i.getCloseApproachData().get(0).getMissDistance()
                                                                .getKilometers())
                                                .relativeVelocityKph(
                                                                i.getCloseApproachData().get(0).getRelativeVelocity()
                                                                                .getKilometersPerHour())
                                                .isHazardous(i.isPotentiallyHazardousAsteroid())
                                                .diameter(new Diameter(
                                                                i.getEstimatedDiameter().getKilometers()
                                                                                .getEstimatedDiameterMin(),
                                                                i.getEstimatedDiameter().getKilometers()
                                                                                .getEstimatedDiameterMax()))
                                                .build())
                                .collect(Collectors.toList());
        }

        public static StandardLookupResponse responseToStandardLookupDto(NeoLookupResponse response) {
                List<ApproachData> appr = new ArrayList<>();
                for (var el : response.getCloseApproachData()) {
                        appr.add(new ApproachData(el.getCloseApproachDateFull(), el.getMissDistance().getKilometers(),
                                        el.getRelativeVelocity().getKilometersPerHour()));
                }
                return StandardLookupResponse.builder()
                                .id(response.getId())
                                .name(response.getName())
                                .diameter(new Diameter(
                                                response.getEstimatedDiameter().getKilometers()
                                                                .getEstimatedDiameterMin(),
                                                response.getEstimatedDiameter().getKilometers()
                                                                .getEstimatedDiameterMax()))
                                .isHazardous(response.isPotentiallyHazardousAsteroid())
                                .approachData(appr)
                                .build();
        }
}
