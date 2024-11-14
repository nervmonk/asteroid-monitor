package sg.ic.asteroidmonitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.ic.asteroidmonitor.dto.BaseResponse;
import sg.ic.asteroidmonitor.dto.StandardFeedResponse;
import sg.ic.asteroidmonitor.service.TopTenInYearService;
import sg.ic.asteroidmonitor.util.ResponseUtil;

@RestController
@RequestMapping("/top-10")
public class TopTenAsteroidsController {
    private final TopTenInYearService topTenInYearService;

    @Autowired
    public TopTenAsteroidsController(TopTenInYearService topTenInYearService){
        this.topTenInYearService = topTenInYearService;
    }

    @GetMapping("/2023")
    public BaseResponse<List<StandardFeedResponse>> getTopTenInYear() {
        return ResponseUtil.success(topTenInYearService.fetchTopTenInYear());
    }
}
