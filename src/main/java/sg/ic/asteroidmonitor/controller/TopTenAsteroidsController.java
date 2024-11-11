package sg.ic.asteroidmonitor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/top-10")
public class TopTenAsteroidsController {

    @GetMapping("/{year}")
    public void getTopTenInYear(@PathVariable("year") String year) {

    }
}
