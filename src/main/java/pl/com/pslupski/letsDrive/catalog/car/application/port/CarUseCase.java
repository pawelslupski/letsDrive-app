package pl.com.pslupski.letsDrive.catalog.car.application.port;

import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import pl.com.pslupski.letsDrive.catalog.car.domain.Car;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface CarUseCase {

    Optional<Car> findById(Long id);

    List<Car> findAll();

    Car addCar(CreateCarCommand commnand);

    UpdateCarResponse updateCar(UpdateCarCommand command);

    void removeById(Long id);

    void updateCarImage(UpdateCarImageCommand command);

    void removeCarImage(Long id);

    @Value
    class CreateCarCommand {
        private String manufacturer;
        private String model;
        private Integer year;
        private Double engine;
        private String fuel;

        public Car toCar() {
            return new Car(manufacturer, model, year, engine, fuel);
        }
    }

    @Value
    class UpdateCarCommand {
        private Long id;
        private String manufacturer;
        private String model;
        private Integer year;
        private Double engine;
        private String fuel;

        public Car updateFields(Car car) {
            if (manufacturer != null) {
                if (StringUtils.isNoneBlank(manufacturer)) {
                    car.setManufacturer(manufacturer);
                }
            }
            if (model != null) {
                if (StringUtils.isNoneBlank(model)) {
                    car.setManufacturer(model);
                }
            }
            if (year != null) {
                car.setYear(year);
            }
            if (engine != null) {
                car.setEngine(engine);
            }
            if (fuel != null) {
                if (StringUtils.isNoneBlank(fuel)) {
                    car.setManufacturer(fuel);
                }
            }
            return car;
        }
    }

    @Value
    class UpdateCarImageCommand {
        Long id;
        String fileName;
        byte[] file;
        String contentType;
    }

    @Value
    class UpdateCarResponse {
        public static UpdateCarResponse SUCCESS = new UpdateCarResponse(true, Collections.emptyList());

        boolean success;
        List<String> errors;
    }
}
