package pl.com.pslupski.letsDrive.catalog.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class Car {
    private Long id;
    private String model;
    private Integer year;
    private BigDecimal price;
    private Integer mileage;

    public Car(String model, Integer year, BigDecimal price, Integer mileage) {
        this.model = model;
        this.year = year;
        this.price = price;
        this.mileage = mileage;
    }
}
