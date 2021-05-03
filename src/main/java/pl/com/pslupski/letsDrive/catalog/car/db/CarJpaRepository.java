package pl.com.pslupski.letsDrive.catalog.car.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.pslupski.letsDrive.catalog.car.domain.Car;

import java.util.Optional;

public interface CarJpaRepository extends JpaRepository<Car, Long> {
    Optional<Car> findByManufacturerAndModelAndYearAndEngineAndFuel(String manufacturer, String model, Integer year,
                                                                    Double engine, String fuel);
}
