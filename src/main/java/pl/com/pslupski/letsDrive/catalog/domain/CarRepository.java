package pl.com.pslupski.letsDrive.catalog.domain;

import java.util.List;
import java.util.Optional;

public interface CarRepository {

    List<Car> findAll();

    Car save(Car car);

    Optional<Car> findById(Long id);

    void removeById(Long id);
}
