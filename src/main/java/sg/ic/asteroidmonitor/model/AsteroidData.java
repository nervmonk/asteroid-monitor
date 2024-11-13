package sg.ic.asteroidmonitor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AsteroidData {
    private String id;
    private String name;
    private String approachDate;
    private String distanceEarthKm;
    private String relativeVelocityKph;
    private Boolean isHazardous;
}
