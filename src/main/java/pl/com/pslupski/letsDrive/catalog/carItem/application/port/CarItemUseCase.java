package pl.com.pslupski.letsDrive.catalog.carItem.application.port;

import lombok.Value;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.Category;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.SubCategory;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CarItemUseCase {

    List<CarItem> findAll();

    Optional<CarItem> findById(Long id);

    Optional<CarItem> findByProductCode(String productCode);

    List<CarItem> findBySubCategory(SubCategory subCategory);

    CarItem addCarItem(CreateCarItemCommand toCreateCommand);

    UpdateCarItemResponse updateCarItem(UpdateCarItemCommand command);

    void removeById(Long id);

    void updateCarItemImage(UpdateCarItemImageCommand command);

    void removeCarItemImage(Long id);

    @Value
    class CreateCarItemCommand {
        private String productCode;
        private BigDecimal price;
        private Category category;
        private SubCategory subCategory;
        private Set<Long> cars;
        private Long available;
    }

    @Value
    class UpdateCarItemCommand {
        private Long id;
        private String productCode;
        private BigDecimal price;
        private Category category;
        private SubCategory subCategory;
        private Set<Long> cars;
    }

    @Value
    class UpdateCarItemImageCommand {
        Long id;
        String fileName;
        byte[] file;
        String contentType;
    }

    @Value
    class UpdateCarItemResponse {
        public static UpdateCarItemResponse SUCCESS = new UpdateCarItemResponse(true, Collections.emptyList());

        boolean success;
        List<String> errors;
    }
}
