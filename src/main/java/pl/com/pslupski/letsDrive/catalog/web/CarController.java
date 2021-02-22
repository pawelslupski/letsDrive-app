package pl.com.pslupski.letsDrive.catalog.web;

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

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.com.pslupski.letsDrive.catalog.application.port.CarUseCase.UpdateCarCommand;
import static pl.com.pslupski.letsDrive.catalog.application.port.CarUseCase.UpdateCarResponse;

@RestController
@RequestMapping("/cars")
@AllArgsConstructor
public class CarController {
    private final CarUseCase catalog;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Car> getAll(
            @RequestParam() Optional<String> model,
            @RequestParam() Optional<Integer> price,
            @RequestParam(defaultValue = "3") int limit) {
        if (model.isPresent() && price.isPresent()) {
            return catalog.findByModelAndPrice(model.get(), price.get());
        } else if (model.isPresent()) {
            return catalog.findByModel(model.get());
        } else if (price.isPresent()) {
            return catalog.findByPrice(price.get());
        }
        return catalog.findAll().stream().limit(limit).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        if (id.equals(102L)) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "I am a teapot!");
        }
        return catalog.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addCar(@Valid @RequestBody RestCarCommand command) {
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
        @NotBlank(message = "Please provide a model")
        private String model;
        @NotNull(message = "Please provide a year")
        @Min(1900)
        private Integer year;
        @NotNull(message = "Please provide a price")
        @DecimalMin("1")
        private BigDecimal price;
        @NotNull(message = "Please provide a mileage")
        private Integer mileage;

        CreateCarCommand toCreateCommand() {
            return new CreateCarCommand(model, year, price, mileage);
        }

        UpdateCarCommand toUpdateCarCommand(Long id) {
            return new UpdateCarCommand(id, model, year, price, mileage);
        }
    }
}
