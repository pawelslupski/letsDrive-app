package pl.com.pslupski.letsDrive.catalog.carItem.application.port;

import lombok.Value;
import pl.com.pslupski.letsDrive.catalog.car.application.port.CarUseCase;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.Category;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.SubCategory;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface CarItemUseCase {

    List<CarItem> findAll();

    Optional<CarItem> findById(Long id);

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

        public CarItem toCarItem() {
            return new CarItem(productCode, price, category, subCategory);
        }
    }

    @Value
    class UpdateCarItemCommand {
        private Long id;
        private String productCode;
        private BigDecimal price;
        private Category category;
        private SubCategory subCategory;

        public CarItem updateFields(CarItem carItem) {
            if (productCode != null) {
                carItem.setProductCode(productCode);
            }
            if (price != null) {
                carItem.setPrice(price);
            }
            if (category != null) {
                carItem.setCategory(category);
            }
            if (subCategory != null) {
                carItem.setSubCategory(subCategory);
            }
            return carItem;
        }
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
