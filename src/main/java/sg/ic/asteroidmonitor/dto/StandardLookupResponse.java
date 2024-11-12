package sg.ic.asteroidmonitor.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import sg.ic.asteroidmonitor.dto.StandardFeedResponse.Diameter;

@Data
@Builder
public class StandardLookupResponse {
    private String id;
    private String name;
    private Diameter diameter;
    private Boolean isHazardous;
    private List<ApproachData> approachData;

    @Data
    @Builder
    @AllArgsConstructor
    public static class ApproachData {
        private String closeApproachDate;
        private String distanceEarthKm;
        private String relativeVelocityKph;

    }

}
