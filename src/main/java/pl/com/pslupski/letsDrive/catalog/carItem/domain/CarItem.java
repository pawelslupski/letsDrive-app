package pl.com.pslupski.letsDrive.catalog.carItem.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.com.pslupski.letsDrive.catalog.car.domain.Car;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private Set<Car> cars;

    public CarItem(String productCode, BigDecimal price, Category category, SubCategory subCategory) {
        this.productCode = productCode;
        this.price = price;
        this.category = category;
        this.subCategory = subCategory;
    }
}
