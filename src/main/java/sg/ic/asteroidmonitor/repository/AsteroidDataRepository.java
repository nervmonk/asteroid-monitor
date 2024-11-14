package sg.ic.asteroidmonitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sg.ic.asteroidmonitor.model.AsteroidData;

public interface AsteroidDataRepository extends JpaRepository<AsteroidData, String> {
    @Query("SELECT ad FROM AsteroidData ad ORDER BY CAST(ad.distanceEarthKm AS double)")
    List<AsteroidData> findAllOrderedByDistance();
}
