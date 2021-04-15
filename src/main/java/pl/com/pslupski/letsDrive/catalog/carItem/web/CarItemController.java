package pl.com.pslupski.letsDrive.catalog.carItem.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.pslupski.letsDrive.catalog.car.application.port.CarUseCase;
import pl.com.pslupski.letsDrive.catalog.carItem.application.port.CarItemUseCase;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.Category;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.SubCategory;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static pl.com.pslupski.letsDrive.catalog.carItem.application.port.CarItemUseCase.*;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class CarItemController {
    private final CarItemUseCase catalog;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CarItem> getAll(
            @RequestParam(defaultValue = "3") int limit) {
        return catalog.findAll().stream().limit(limit).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return catalog.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addCarItem(@Valid @RequestBody RestCarItemCommand command) {
        CarItem carItem = catalog.addCarItem(command.toCreateCommand());
        URI uri = createdCarItemUri(carItem);
        return ResponseEntity.created(uri).build();
    }

    private URI createdCarItemUri(CarItem carItem) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/" + carItem.getId().toString()).build().toUri();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateCarItem(@PathVariable Long id, @RequestBody RestCarItemCommand command) {
        UpdateCarItemResponse response = catalog.updateCarItem(command.toUpdateCarItemCommand(id));
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCarItem(@PathVariable Long id) {
        catalog.removeById(id);
    }

    @PutMapping(value = "/{id}/cover")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCarItemImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        catalog.updateCarItemImage(new UpdateCarItemImageCommand(
                id,
                file.getOriginalFilename(),
                file.getBytes(),
                file.getContentType()));
    }

    @DeleteMapping(value = "/{id}/cover")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCarItemImage(@PathVariable Long id) {
        catalog.removeCarItemImage(id);
    }

    @Data
    private static class RestCarItemCommand {
        @NotBlank(message = "Please provide a productCode")
        private String productCode;
        @NotNull
        @DecimalMin("0.00")
        private BigDecimal price;
        @NotNull(message = "Please provide a category")
        private Category category;
        private SubCategory subCategory;

        CreateCarItemCommand toCreateCommand() {
            return new CreateCarItemCommand(productCode, price, category, subCategory);
        }

        UpdateCarItemCommand toUpdateCarItemCommand(Long id) {
            return new UpdateCarItemCommand(id, productCode, price, category, subCategory);
        }
    }
}