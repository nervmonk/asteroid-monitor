package sg.ic.asteroidmonitor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * DTO that contains information about near-earth-object Asteroids that's relevant for general population (in my opinion)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StandardFeedResponse {
    private String id;
    private String name;
    private String approachDate;
    private String distanceFromEarthKm;
    private String relativeVelocityKph;
    private Diameter diameter;
    private Boolean isHazardous;

    @Data
    @AllArgsConstructor
    public static class Diameter {
        private Double min;
        private Double max;
    }
}
