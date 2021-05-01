package pl.com.pslupski.letsDrive.catalog.car.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@ToString(exclude = "carItems")
public class Car {
    @Id
    @GeneratedValue
    private Long id;
    private String model;
    private Integer year;
    private Double engine;
    private String fuel;
    private String manufacturer;
    private Long imageId;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "cars")
    @JsonIgnoreProperties("cars")
    private Set<CarItem> carItems;
    @CreatedDate
    private LocalDateTime createdAt;

    public Car(String manufacturer, String model, Integer year, Double engine, String fuel) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.year = year;
        this.engine = engine;
        this.fuel = fuel;
    }
}
