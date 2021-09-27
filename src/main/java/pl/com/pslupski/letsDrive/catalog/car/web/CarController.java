package pl.com.pslupski.letsDrive.catalog.car.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.pslupski.letsDrive.catalog.car.application.port.CarUseCase;
import pl.com.pslupski.letsDrive.catalog.car.application.port.CarUseCase.CreateCarCommand;
import pl.com.pslupski.letsDrive.catalog.car.application.port.CarUseCase.UpdateCarCommand;
import pl.com.pslupski.letsDrive.catalog.car.application.port.CarUseCase.UpdateCarImageCommand;
import pl.com.pslupski.letsDrive.catalog.car.application.port.CarUseCase.UpdateCarResponse;
import pl.com.pslupski.letsDrive.catalog.car.domain.Car;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cars")
@AllArgsConstructor
public class CarController {
    private final CarUseCase catalog;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Car> getAll(
            @RequestParam(defaultValue = "3") int limit) {
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

    @Secured({"ROLE_ADMIN"})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addCar(@Validated(CreateValidation.class) @RequestBody RestCarCommand command) {
        Car car = catalog.addCar(command.toCreateCommand());
        URI uri = createdCarUri(car);
        return ResponseEntity.created(uri).build();
    }

    private URI createdCarUri(Car car) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/" + car.getId().toString()).build().toUri();
    }

    @Secured({"ROLE_ADMIN"})
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateCar(@PathVariable Long id, @Validated(UpdateValidation.class) @RequestBody RestCarCommand command) {
        UpdateCarResponse response = catalog.updateCar(command.toUpdateCarCommand(id));
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable Long id) {
        catalog.removeById(id);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "/{id}/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public void addCarImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        catalog.updateCarImage(new UpdateCarImageCommand(
                id,
                file.getOriginalFilename(),
                file.getBytes(),
                file.getContentType()));
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "/{id}/image")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCarImage(@PathVariable Long id) {
        catalog.removeCarImage(id);
    }

    interface UpdateValidation {
    }

    interface CreateValidation {
    }

    @Data
    private static class RestCarCommand {
        @NotBlank(message = "Please provide a manufacturer", groups = {CreateValidation.class})
        private String manufacturer;
        @NotBlank(message = "Please provide a model", groups = {CreateValidation.class})
        private String model;
        @NotNull(message = "Please provide a year", groups = {CreateValidation.class})
        @Min(value = 1900, groups = {CreateValidation.class, UpdateValidation.class})
        private Integer year;
        @NotNull(message = "Please provide the engine capacity", groups = {CreateValidation.class})
        @DecimalMin(value = "1.0", groups = {CreateValidation.class, UpdateValidation.class})
        private Double engine;
        @NotBlank(message = "Please define the engine fuel type", groups = {CreateValidation.class})
        private String fuel;

        CreateCarCommand toCreateCommand() {
            return new CreateCarCommand(manufacturer, model, year, engine, fuel);
        }

        UpdateCarCommand toUpdateCarCommand(Long id) {
            return new UpdateCarCommand(id, manufacturer, model, year, engine, fuel);
        }
    }
}