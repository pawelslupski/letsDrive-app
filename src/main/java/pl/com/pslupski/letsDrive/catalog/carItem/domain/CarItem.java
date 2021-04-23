package pl.com.pslupski.letsDrive.catalog.carItem.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class CarItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productCode;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Enumerated(EnumType.STRING)
    private SubCategory subCategory;
    private Long imageId;
//    private Car car; // TODO relation between carItem and specific car

    public CarItem(String productCode, BigDecimal price, Category category, SubCategory subCategory) {
        this.productCode = productCode;
        this.price = price;
        this.category = category;
        this.subCategory = subCategory;
    }
}
