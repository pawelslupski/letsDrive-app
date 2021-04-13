package pl.com.pslupski.letsDrive.catalog.car.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Car {
    private Long id;
    private String manufacturer;
    private String model;
    private Integer year;
    private Double engine;
    private String fuel;
    private String imageId;

    public Car(String manufacturer, String model, Integer year, Double engine, String fuel) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.year = year;
        this.engine = engine;
        this.fuel = fuel;
    }
}
