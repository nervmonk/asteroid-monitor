package sg.ic.asteroidmonitor.controller;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.ic.asteroidmonitor.dto.BaseResponse;
import sg.ic.asteroidmonitor.dto.FeedDateRangeReq;
import sg.ic.asteroidmonitor.dto.StandardFeedResponse;
import sg.ic.asteroidmonitor.dto.StandardLookupResponse;
import sg.ic.asteroidmonitor.service.NeoService;
import sg.ic.asteroidmonitor.service.ValidationService;
import sg.ic.asteroidmonitor.util.ResponseUtil;

@RestController
@RequestMapping("/neo")
public class NeoController {

    private final NeoService neoService;
    private final ValidationService validationService;

    @Autowired
    public NeoController(NeoService neoService, ValidationService validationService) {
        this.neoService = neoService;
        this.validationService = validationService;
    }

    @PostMapping("/feed")
    public BaseResponse<List<StandardFeedResponse>> getFeed(@RequestBody FeedDateRangeReq request) {
        validationService.validate(request);
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("startDate must be same or before endDate");
        }
        if (ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) > 7) {
            throw new IllegalArgumentException("The date range must not exceed 7 days");
        }
        return ResponseUtil.success(neoService.getFeed(request));
    }

    @GetMapping("/lookup/{id}")
    public BaseResponse<StandardLookupResponse> getLookUp(@PathVariable("id") String id) {
        return ResponseUtil.success(neoService.getLookUp(id));
    }
}
