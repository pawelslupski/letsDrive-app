package pl.com.pslupski.letsDrive.catalog.carItem.application;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pl.com.pslupski.letsDrive.catalog.car.db.CarJpaRepository;
import pl.com.pslupski.letsDrive.catalog.car.domain.Car;
import pl.com.pslupski.letsDrive.catalog.carItem.application.port.CarItemUseCase;
import pl.com.pslupski.letsDrive.catalog.carItem.db.CarItemJpaRepository;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.SubCategory;
import pl.com.pslupski.letsDrive.uploads.application.port.UploadUseCase;
import pl.com.pslupski.letsDrive.uploads.domain.Upload;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.com.pslupski.letsDrive.uploads.application.port.UploadUseCase.SaveUploadCommand;

@Service
@AllArgsConstructor
public class CarItemService implements CarItemUseCase {
    private final CarItemJpaRepository carItemRepository;
    private final CarJpaRepository carRepository;
    private final UploadUseCase upload;

    @Override
    public List<CarItem> findAll() {
        return carItemRepository.findAll();
    }

    @Override
    public Optional<CarItem> findById(Long id) {
        return carItemRepository.findById(id);
    }

    @Override
    public Optional<CarItem> findByProductCode(String productCode) {
        return carItemRepository.findByProductCode(productCode);
    }

    @Override
    public List<CarItem> findBySubCategory(SubCategory subCategory) {
        return carItemRepository.findBySubCategory(subCategory);
    }

    @Override
    public CarItem addCarItem(CreateCarItemCommand command) {
        CarItem carItem = toCarItem(command);
        return carItemRepository.save(carItem);
    }

    private CarItem toCarItem(CreateCarItemCommand command) {
        CarItem carItem = new CarItem(command.getProductCode(), command.getPrice(), command.getCategory(),
                command.getSubCategory());
        Set<Car> cars = fetchCarsByIds(command.getCars());
        updateCarsInCarItem(carItem, cars);
        return carItem;
    }

    private void updateCarsInCarItem(CarItem carItem, Set<Car> cars) {
        carItem.removeCars();
        cars.forEach(carItem::setCar);
    }

    @Override
    public UpdateCarItemResponse updateCarItem(UpdateCarItemCommand command) {
        return carItemRepository.findById(command.getId())
                .map(carItem -> {
                    CarItem updatedCarItem = updateFields(command, carItem);
                    carItemRepository.save(updatedCarItem);
                    return UpdateCarItemResponse.SUCCESS;
                }).orElseGet(() -> new UpdateCarItemResponse(false,
                        Collections.singletonList("Car item not found with id: " + command.getId())));
    }

    private Set<Car> fetchCarsByIds(Set<Long> cars) {
        return cars.stream()
                .map(carId -> carRepository.findById(carId)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to find car with id")))
                .collect(Collectors.toSet());
    }

    public CarItem updateFields(UpdateCarItemCommand command, CarItem carItem) {
        if (command.getProductCode() != null) {
            if (StringUtils.isNoneBlank(command.getProductCode())) {
                carItem.setProductCode(command.getProductCode());
            }
        }
        if (command.getCars() != null && !command.getCars().isEmpty()) {
            updateCarsInCarItem(carItem, fetchCarsByIds(Collections.unmodifiableSet(command.getCars())));
        }
        if (command.getPrice() != null) {
            carItem.setPrice(command.getPrice());
        }
        if (command.getCategory() != null) {
            carItem.setCategory(command.getCategory());
        }
        if (command.getSubCategory() != null) {
            carItem.setSubCategory(command.getSubCategory());
        }
        return carItem;
    }

    @Override
    public void removeById(Long id) {
        carItemRepository.deleteById(id);
    }

    @Override
    public void updateCarItemImage(UpdateCarItemImageCommand command) {
        carItemRepository.findById(command.getId())
                .ifPresent(carItem -> {
                    Upload savedUpload = upload.save(new SaveUploadCommand(command.getFileName(),
                            command.getFile(), command.getContentType()));
                    carItem.setImageId(savedUpload.getId());
                    carItemRepository.save(carItem);
                });
    }

    @Override
    public void removeCarItemImage(Long id) {
        carItemRepository.findById(id)
                .ifPresent(carItem -> {
                    if (carItem.getImageId() != null) {
                        upload.removeById(carItem.getImageId());
                    }
                    carItem.setImageId(null);
                    carItemRepository.save(carItem);
                });
    }
}
