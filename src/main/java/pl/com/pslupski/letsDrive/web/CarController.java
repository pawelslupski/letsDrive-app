package pl.com.pslupski.letsDrive.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.pslupski.letsDrive.catalog.application.port.CarUseCase;
import pl.com.pslupski.letsDrive.catalog.application.port.CarUseCase.CreateCarCommand;
import pl.com.pslupski.letsDrive.catalog.domain.Car;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import static pl.com.pslupski.letsDrive.catalog.application.port.CarUseCase.*;

@RestController
@RequestMapping("/cars")
@AllArgsConstructor
public class CarController {
    private final CarUseCase catalog;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Car> getAll() {
        return catalog.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return catalog.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addCar(@RequestBody RestCarCommand command) {
        Car car = catalog.addCar(command.toCreateCommand());
        URI uri = createdCarUri(car);
        return ResponseEntity.created(uri).build();
    }

    private URI createdCarUri(Car car) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/" + car.getId().toString()).build().toUri();
    }

    @PutMapping("{/id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateCar(@PathVariable Long id, @RequestBody RestCarCommand command) {
        UpdateCarResponse response = catalog.updateCar(command.toUpdateCarCommand(id));
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable Long id) {
        catalog.removeById(id);
    }

    @Data
    private static class RestCarCommand {
        private String model;
        private Integer year;
        private BigDecimal price;
        private Integer mileage;

        CreateCarCommand toCreateCommand() {
            return new CreateCarCommand(model, year, price, mileage);
        }

        UpdateCarCommand toUpdateCarCommand(Long id) {
            return new UpdateCarCommand(id, model, year, price, mileage);
        }
    }
}
