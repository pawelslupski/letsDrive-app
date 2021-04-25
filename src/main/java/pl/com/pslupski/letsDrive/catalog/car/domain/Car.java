package pl.com.pslupski.letsDrive.catalog.car.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
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
    private Set<CarItem> carItems;

    public Car(String manufacturer, String model, Integer year, Double engine, String fuel) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.year = year;
        this.engine = engine;
        this.fuel = fuel;
    }
}
