package pl.com.pslupski.letsDrive.catalog.application.port;

import lombok.Value;
import pl.com.pslupski.letsDrive.catalog.domain.Car;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface CarUseCase {

    Optional<Car> findById(Long id);

    List<Car> findAll();

    Car addCar(CreateCarCommand commnand);

    UpdateCarResponse updateCar(UpdateCarCommand command);

    void removeById(Long id);

    @Value
    class CreateCarCommand {
        private String model;
        private Integer year;
        private BigDecimal price;
        private int mileage;

        public Car toCar() {
            return new Car(model, year, price, mileage);
        }
    }

    @Value
    class UpdateCarCommand {
        private Long id;
        private String model;
        private Integer year;
        private BigDecimal price;
        private Integer mileage;

        public Car updateFields(Car car) {
            if (model != null) {
                car.setModel(model);
            }
            if (year != null) {
                car.setYear(year);
            }
            if (price != null) {
                car.setPrice(price);
            }
            if (mileage != null) {
                car.setMileage(mileage);
            }
            return car;
        }
    }

    @Value
    class UpdateCarResponse {
        public static UpdateCarResponse SUCCESS = new UpdateCarResponse(true, Collections.emptyList());

        boolean success;
        List<String> errors;
    }
}
