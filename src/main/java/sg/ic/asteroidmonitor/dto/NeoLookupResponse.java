package sg.ic.asteroidmonitor.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NeoLookupResponse {
    private Links links;

    private String id;

    @JsonProperty("neo_reference_id")
    private String neoReferenceId;

    private String name;

    @JsonProperty("nasa_jpl_url")
    private String nasaJplUrl;

    @JsonProperty("absolute_magnitude_h")
    private double absoluteMagnitudeH;

    @JsonProperty("estimated_diameter")
    private EstimatedDiameter estimatedDiameter;

    @JsonProperty("is_potentially_hazardous_asteroid")
    private boolean isPotentiallyHazardousAsteroid;

    @JsonProperty("close_approach_data")
    private List<CloseApproachData> closeApproachData;

    @JsonProperty("is_sentry_object")
    private boolean isSentryObject;

    @Data
    public static class Links {
        private String self;
    }

    @Data
    public static class EstimatedDiameter {
        private Diameter kilometers;
        private Diameter meters;
        private Diameter miles;
        private Diameter feet;
    }

    @Data
    public static class Diameter {
        @JsonProperty("estimated_diameter_min")
        private double estimatedDiameterMin;

        @JsonProperty("estimated_diameter_max")
        private double estimatedDiameterMax;
    }

    @Data
    public static class CloseApproachData {
        @JsonProperty("close_approach_date")
        private String closeApproachDate;

        @JsonProperty("close_approach_date_full")
        private String closeApproachDateFull;

        @JsonProperty("epoch_date_close_approach")
        private long epochDateCloseApproach;

        @JsonProperty("relative_velocity")
        private RelativeVelocity relativeVelocity;

        @JsonProperty("miss_distance")
        private MissDistance missDistance;

        @JsonProperty("orbiting_body")
        private String orbitingBody;
    }

    @Data
    public static class RelativeVelocity {
        @JsonProperty("kilometers_per_second")
        private String kilometersPerSecond;

        @JsonProperty("kilometers_per_hour")
        private String kilometersPerHour;

        @JsonProperty("miles_per_hour")
        private String milesPerHour;
    }

    @Data
    public static class MissDistance {
        private String astronomical;
        private String lunar;
        private String kilometers;
        private String miles;
    }

}
