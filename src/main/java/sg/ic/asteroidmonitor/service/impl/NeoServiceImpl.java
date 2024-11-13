package sg.ic.asteroidmonitor.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import sg.ic.asteroidmonitor.dto.FeedDateRangeReq;
import sg.ic.asteroidmonitor.dto.NeoFeedResponse;
import sg.ic.asteroidmonitor.dto.StandardFeedResponse;
import sg.ic.asteroidmonitor.dto.StandardLookupResponse;
import sg.ic.asteroidmonitor.dto.NeoFeedResponse.NearEarthObject;
import sg.ic.asteroidmonitor.feign.NasaNeoClient;
import sg.ic.asteroidmonitor.service.NeoService;
import sg.ic.asteroidmonitor.util.FilterResponseUtil;

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

                return FilterResponseUtil.getTop10Closest(neo);
        }

        @Override
        public StandardLookupResponse getLookUp(String id) {
                return FilterResponseUtil.responseToStandardLookupDto(nasaNeoClient.getLookUp(id, "DEMO_KEY"));
        }
}
