package sg.ic.asteroidmonitor.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "asteroid_data")
public class AsteroidData {
    @Id
    private String id;

    private String name;

    @Column(name = "approach_date")
    private String approachDate;

    @Column(name = "distance_from_earth_km")
    private String distanceEarthKm;

    @Column(name = "relative_velocity_kph")
    private String relativeVelocityKph;

    @Column(name = "is_hazardous")
    private Boolean isHazardous;

    @OneToOne(mappedBy = "asteroidData", cascade = CascadeType.ALL, orphanRemoval = true)
    private Diameter diameter;
}
