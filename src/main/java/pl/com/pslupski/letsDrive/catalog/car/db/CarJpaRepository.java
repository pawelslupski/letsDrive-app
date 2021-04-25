package pl.com.pslupski.letsDrive.catalog.car.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.pslupski.letsDrive.catalog.car.domain.Car;

public interface CarJpaRepository extends JpaRepository<Car, Long> {
}
