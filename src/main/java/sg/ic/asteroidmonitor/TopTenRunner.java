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
import sg.ic.asteroidmonitor.model.AsteroidData;
import sg.ic.asteroidmonitor.model.Diameter;
import sg.ic.asteroidmonitor.repository.AsteroidDataRepository;
import sg.ic.asteroidmonitor.util.FilterResponseUtil;

/*
 * This class has intention to populating database with top 10 closest asteroid from earth in 2023
 * It is designed to run once when the application starts, and check conditions whether the data are already populated before making request to external resources (nasa public api)
 */
@Component
@Slf4j
public class TopTenRunner implements CommandLineRunner {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final NasaNeoClient nasaNeoClient;
    private final AsteroidDataRepository asteroidDataRepository;

    @Autowired
    public TopTenRunner(NasaNeoClient nasaNeoClient, AsteroidDataRepository asteroidDataRepository) {
        this.nasaNeoClient = nasaNeoClient;
        this.asteroidDataRepository = asteroidDataRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // No need to re-execute if db are already populated
        if (asteroidDataRepository.count() == 0) {
            List<NearEarthObject> top10inYear = new ArrayList<>();
            LocalDate startDate = LocalDate.of(2023, 1, 1);
            LocalDate endDate = startDate.plusYears(1);

            while (startDate.isBefore(endDate)) {
                String start;
                String end;
                // Condition to make sure query are precisely bound within one year
                if (startDate.plusDays(7).isEqual(endDate) || startDate.plusDays(7).isAfter(endDate)) {
                    start = startDate.format(FORMATTER);
                    end = LocalDate.of(2023, 12, 31).format(FORMATTER);
                    log.info(start);
                    log.info(end);
                } else {
                    start = startDate.format(FORMATTER);
                    end = startDate.plusDays(7).format(FORMATTER);
                    log.info(start);
                    log.info(end);
                }

                startDate = startDate.plusDays(8);

                NeoFeedResponse apiRes = nasaNeoClient.getNeoFeed(start,
                        end, "DEMO_KEY");

                List<NearEarthObject> neo = apiRes.getNearEarthObjects().values().stream()
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                top10inYear.addAll(neo);
                top10inYear = FilterResponseUtil.getTop10ClosestInYear(top10inYear);
                log.info("");
            }

            var collectedData = FilterResponseUtil.responseToStandardFeedDto(top10inYear);

            log.info("{}", collectedData);
            saveToDb(collectedData);
        }

    }

    public void saveToDb(List<StandardFeedResponse> responses) {
        var savedData = asteroidDataRepository.saveAll(responses.stream()
                .map(response -> AsteroidData.builder()
                        .id(response.getId())
                        .name(response.getName())
                        .approachDate(response.getApproachDate())
                        .distanceEarthKm(response.getDistanceFromEarthKm())
                        .relativeVelocityKph(response.getRelativeVelocityKph())
                        .isHazardous(response.getIsHazardous())
                        .diameter(Diameter.builder()
                                .asteroidId(response.getId())
                                .min(response.getDiameter().getMin())
                                .max(response.getDiameter().getMax())
                                .build())
                        .build())
                .collect(Collectors.toList()));
        if (!savedData.isEmpty()) {
            log.info("Success saved 2023 data");
        }
    }

}
