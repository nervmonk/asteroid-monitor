package sg.ic.asteroidmonitor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Diameter {
    private String id;
    private Double min;
    private Double max;
}
