package pl.com.pslupski.letsDrive.catalog.carItem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.com.pslupski.letsDrive.catalog.car.domain.Car;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = "cars")
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
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
    @JsonIgnoreProperties("carItems")
    private Set<Car> cars;
    @CreatedDate
    private LocalDateTime createdAt;

    public CarItem(String productCode, BigDecimal price, Category category, SubCategory subCategory) {
        this.productCode = productCode;
        this.price = price;
        this.category = category;
        this.subCategory = subCategory;
    }
}
