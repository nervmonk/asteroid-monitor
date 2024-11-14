package sg.ic.asteroidmonitor.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.ic.asteroidmonitor.dto.StandardFeedResponse;
import sg.ic.asteroidmonitor.dto.StandardFeedResponse.Diameter;
import sg.ic.asteroidmonitor.repository.AsteroidDataRepository;
import sg.ic.asteroidmonitor.service.TopTenInYearService;

@Service
public class TopTenInYearServiceImpl implements TopTenInYearService {
    private final AsteroidDataRepository asteroidDataRepository;

    @Autowired
    public TopTenInYearServiceImpl(AsteroidDataRepository asteroidDataRepository) {
        this.asteroidDataRepository = asteroidDataRepository;
    }

    @Override
    public List<StandardFeedResponse> fetchTopTenInYear() {
        var responseFromDb = asteroidDataRepository.findAllOrderedByDistance();
        return responseFromDb.stream()
                .map(res -> StandardFeedResponse.builder()
                        .id(res.getId())
                        .name(res.getName())
                        .approachDate(res.getApproachDate())
                        .distanceFromEarthKm(res.getDistanceEarthKm())
                        .relativeVelocityKph(res.getRelativeVelocityKph())
                        .diameter(new Diameter(res.getDiameter().getMin(), res.getDiameter().getMax()))
                        .isHazardous(res.getIsHazardous())
                        .build())
                .collect(Collectors.toList());
    }

}
