package sg.ic.asteroidmonitor.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sg.ic.asteroidmonitor.dto.FeedDateRangeReq;
import sg.ic.asteroidmonitor.dto.NeoFeedResponse.NearEarthObject;
import sg.ic.asteroidmonitor.service.NeoService;

@RestController
@RequestMapping("/neo")
public class NeoController {

    private final NeoService neoService;

    @Autowired
    public NeoController(NeoService neoService) {
        this.neoService = neoService;
    }

    @PostMapping("/feed")
    public List<NearEarthObject> getFeed(@Valid @RequestBody FeedDateRangeReq request) {
        return neoService.getFeed(request);
    }

    @GetMapping("/lookup/{id}")
    public Map<String, Object> getLookUp(@PathVariable("id") String id) {
        return neoService.getLookUp(id);
    }
}
