package sg.ic.asteroidmonitor.service;

import java.util.List;

import sg.ic.asteroidmonitor.dto.StandardFeedResponse;

public interface TopTenInYearService {
    List<StandardFeedResponse> fetchTopTenInYear();
}
