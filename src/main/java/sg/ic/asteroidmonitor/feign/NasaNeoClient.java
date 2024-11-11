package sg.ic.asteroidmonitor.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import sg.ic.asteroidmonitor.dto.NeoFeedResponse;

@FeignClient(name = "nasaNeoClient", url = "https://api.nasa.gov/neo/rest/v1")
public interface NasaNeoClient {

    @GetMapping("/feed")
    NeoFeedResponse getNeoFeed(
            @RequestParam("start_date") String startDate,
            @RequestParam("end_date") String endDate,
            @RequestParam("api_key") String apiKey);

    @GetMapping("/neo/{id}")
    Map<String, Object> getLookUp(
            @PathVariable("id") String id,
            @RequestParam("api_key") String apiKey);
}
