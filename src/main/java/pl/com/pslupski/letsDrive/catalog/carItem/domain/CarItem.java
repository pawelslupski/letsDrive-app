package pl.com.pslupski.letsDrive.catalog.carItem.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class CarItem {
    @Id
    private Long id;
    private String productCode;
    private BigDecimal price;
    private Category category;
    private SubCategory subCategory;
    private String imageId;
//    private Car car; // TODO relation between carItem and specific car

    public CarItem(String productCode, BigDecimal price, Category category, SubCategory subCategory) {
        this.productCode = productCode;
        this.price = price;
        this.category = category;
        this.subCategory = subCategory;
    }
}
