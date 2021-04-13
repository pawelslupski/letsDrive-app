package pl.com.pslupski.letsDrive.catalog.carItem.domain;

import java.util.List;
import java.util.Optional;

public interface CarItemRepository {

    List<CarItem> findAll();

    CarItem save(CarItem item);

    Optional<CarItem> findById(Long id);

    void removeById(Long id);
}
