package pl.com.pslupski.letsDrive.catalog.carItem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.com.pslupski.letsDrive.catalog.car.domain.Car;
import pl.com.pslupski.letsDrive.jpa.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = "cars")
public class CarItem extends BaseEntity {
    @Column(unique = true)
    private String productCode;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Enumerated(EnumType.STRING)
    private SubCategory subCategory;
    private Long imageId;
    private Long available;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable
    @JsonIgnoreProperties("carItems")
    private Set<Car> cars = new HashSet<>();
    @CreatedDate
    private LocalDateTime createdAt;

    public CarItem(String productCode, BigDecimal price, Category category, SubCategory subCategory, Long available) {
        this.productCode = productCode;
        this.price = price;
        this.category = category;
        this.subCategory = subCategory;
        this.available = available;
    }

    public void setCar(Car car) {
        cars.add(car);
        car.getCarItems().add(this);
    }

    public void removeCar(Car car) {
        cars.remove(car);
        car.getCarItems().remove(this);
    }

    public void removeCars() {
        cars.forEach(car -> car.getCarItems().remove(this));
        cars.clear();
    }
}