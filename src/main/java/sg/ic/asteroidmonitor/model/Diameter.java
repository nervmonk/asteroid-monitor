package sg.ic.asteroidmonitor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "asteroid_diameter")
public class Diameter {
    @Id
    private String asteroidId;

    @Column(name = "min_diameter")
    private Double min;

    @Column(name = "max_diameter")
    private Double max;

    @OneToOne
    @JoinColumn(name = "asteroid_id", referencedColumnName = "id")
    private AsteroidData asteroidData;
}
