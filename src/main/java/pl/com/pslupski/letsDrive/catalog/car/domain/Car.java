package pl.com.pslupski.letsDrive.catalog.car.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.jpa.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "carItems")
public class Car extends BaseEntity {
    private String model;
    private Integer year;
    private Double engine;
    private String fuel;
    private String manufacturer;
    private String plateNumber;
    private Long imageId;
    @ManyToMany(mappedBy = "cars", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("cars")
    private Set<CarItem> carItems = new HashSet<>();
    @CreatedDate
    private LocalDateTime createdAt;

    public Car(String manufacturer, String model, Integer year, Double engine, String fuel) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.year = year;
        this.engine = engine;
        this.fuel = fuel;
    }

    public void setCarItem(CarItem carItem) {
        carItems.add(carItem);
        carItem.setCar(this);
    }

    public void removeCarItem(CarItem carItem) {
        carItems.remove(carItem);
        carItem.getCars().remove(this);
    }
}
