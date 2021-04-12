package pl.com.pslupski.letsDrive.catalog.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.pslupski.letsDrive.catalog.application.port.CarUseCase;
import pl.com.pslupski.letsDrive.catalog.domain.Car;
import pl.com.pslupski.letsDrive.catalog.domain.CarRepository;
import pl.com.pslupski.letsDrive.uploads.application.port.UploadUseCase;
import pl.com.pslupski.letsDrive.uploads.application.port.UploadUseCase.SaveUploadCommand;
import pl.com.pslupski.letsDrive.uploads.domain.Upload;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CarService implements CarUseCase {
    private final CarRepository repository;
    private final UploadUseCase upload;

    @Override
    public Optional<Car> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Car> findAll() {
        return repository.findAll();
    }

    @Override
    public Car addCar(CreateCarCommand command) {
        Car car = command.toCar();
        return repository.save(car);
    }

    @Override
    public UpdateCarResponse updateCar(UpdateCarCommand command) {
        return repository.findById(command.getId())
                .map(car -> {
                    Car updatedCar = command.updateFields(car);
                    repository.save(updatedCar);
                    return UpdateCarResponse.SUCCESS;
                }).orElseGet(() -> new UpdateCarResponse(false,
                        Collections.singletonList("Car not found with id: " + command.getId())));
    }

    @Override
    public void removeById(Long id) {
        repository.removeById(id);
    }

    @Override
    public void updateCarImage(UpdateCarImageCommand command) {
        repository.findById(command.getId())
                .ifPresent(car -> {
                    Upload savedUpload = upload.save(new SaveUploadCommand(command.getFileName(),
                            command.getFile(), command.getContentType()));
                    car.setImageId(savedUpload.getId());
                    repository.save(car);
                });
    }

    @Override
    public void removeCarImage(Long id) {
        repository.findById(id)
                .ifPresent(car -> {
                    if (car.getImageId() != null) {
                        upload.removeById(car.getImageId());
                    }
                    car.setImageId(null);
                    repository.save(car);
                });
    }
}
