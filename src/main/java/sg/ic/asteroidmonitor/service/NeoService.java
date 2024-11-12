package sg.ic.asteroidmonitor.service;

import java.util.List;

import sg.ic.asteroidmonitor.dto.FeedDateRangeReq;
import sg.ic.asteroidmonitor.dto.StandardFeedResponse;
import sg.ic.asteroidmonitor.dto.StandardLookupResponse;

public interface NeoService {
    List<StandardFeedResponse> getFeed(FeedDateRangeReq request);

    StandardLookupResponse getLookUp(String id);
}
