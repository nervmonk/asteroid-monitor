package sg.ic.asteroidmonitor.service;

import java.util.List;
import java.util.Map;

import sg.ic.asteroidmonitor.dto.FeedDateRangeReq;
import sg.ic.asteroidmonitor.dto.NeoFeedResponse.NearEarthObject;

public interface NeoService {
    List<NearEarthObject> getFeed(FeedDateRangeReq request);

    Map<String, Object> getLookUp(String id);
}
