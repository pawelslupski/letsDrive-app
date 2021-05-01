package pl.com.pslupski.letsDrive.catalog.carItem.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.SubCategory;

import java.util.List;
import java.util.Optional;

public interface CarItemJpaRepository extends JpaRepository<CarItem, Long> {

    @Query("SELECT c from CarItem c JOIN FETCH c.cars")
    List<CarItem> findAllEager();

    Optional<CarItem> findByProductCode(String productCode);

    List<CarItem> findBySubCategory(SubCategory subCategory);
}
