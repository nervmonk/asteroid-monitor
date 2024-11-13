package sg.ic.asteroidmonitor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import sg.ic.asteroidmonitor.dto.NeoFeedResponse;
import sg.ic.asteroidmonitor.dto.NeoFeedResponse.NearEarthObject;
import sg.ic.asteroidmonitor.dto.StandardFeedResponse;
import sg.ic.asteroidmonitor.feign.NasaNeoClient;
import sg.ic.asteroidmonitor.util.FilterResponseUtil;

@Component
@Slf4j
public class TopTenRunner implements CommandLineRunner {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final NasaNeoClient nasaNeoClient;

    @Autowired
    public TopTenRunner(NasaNeoClient nasaNeoClient) {
        this.nasaNeoClient = nasaNeoClient;
    }

    // Method for querying nasa asteroid data for year 2023, filter the top 10
    // closest to earth, and save it to the databases
    // @Override
    // public void run(String... args) throws Exception {
    // List<NearEarthObject> top10inYear = new ArrayList<>();
    // LocalDate startDate = LocalDate.of(2023, 1, 1);
    // LocalDate endDate = startDate.plusYears(1);

    // while (startDate.isBefore(endDate)) {
    // String start;
    // String end;
    // // Condition to make sure query are precisely bound within one year
    // if (startDate.plusDays(7).isEqual(endDate) ||
    // startDate.plusDays(7).isAfter(endDate)) {
    // start = startDate.format(FORMATTER);
    // end = LocalDate.of(2023, 12, 31).format(FORMATTER);
    // System.out.println(start);
    // System.out.println(end);
    // } else {
    // start = startDate.format(FORMATTER);
    // end = startDate.plusDays(7).format(FORMATTER);
    // System.out.println(start);
    // System.out.println(end);
    // }

    // startDate = startDate.plusDays(8);

    // NeoFeedResponse apiRes = nasaNeoClient.getNeoFeed(start,
    // end, "DEMO_KEY");

    // List<NearEarthObject> neo = apiRes.getNearEarthObjects().values().stream()
    // .flatMap(List::stream)
    // .collect(Collectors.toList());
    // top10inYear.addAll(neo);
    // top10inYear = FilterResponseUtil.getTop10ClosestInYear(top10inYear);
    // System.out.println();
    // }
    // }

    @Override
    public void run(String... args) throws Exception {
        List<NearEarthObject> top10inYear = new ArrayList<>();
        LocalDate startDate = LocalDate.of(2020, 9, 1);
        LocalDate endDate = startDate.plusMonths(1);

        while (startDate.isBefore(endDate)) {
            String start;
            String end;
            // Condition to make sure query are precisely bound within one year
            if (startDate.plusDays(7).isEqual(endDate) || startDate.plusDays(7).isAfter(endDate)) {
                start = startDate.format(FORMATTER);
                end = LocalDate.of(2020, 9, 30).format(FORMATTER);
                System.out.println(start);
                System.out.println(end);
            } else {
                start = startDate.format(FORMATTER);
                end = startDate.plusDays(7).format(FORMATTER);
                System.out.println(start);
                System.out.println(end);
            }

            startDate = startDate.plusDays(8);

            NeoFeedResponse apiRes = nasaNeoClient.getNeoFeed(start,
                    end, "DEMO_KEY");

            List<NearEarthObject> neo = apiRes.getNearEarthObjects().values().stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            top10inYear.addAll(neo);
            top10inYear = FilterResponseUtil.getTop10ClosestInYear(top10inYear);
            System.out.println();
        }

        System.out.println(FilterResponseUtil.responseToStandardFeedDto(top10inYear));
    }

}
