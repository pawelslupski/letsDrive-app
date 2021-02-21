package pl.com.pslupski.letsDrive.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.pslupski.letsDrive.application.port.CarUseCase;
import pl.com.pslupski.letsDrive.catalog.domain.Car;
import pl.com.pslupski.letsDrive.catalog.domain.CarRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CarService implements CarUseCase {
    private final CarRepository repository;

    @Override
    public Optional<Car> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Car> findAll() {
        return repository.findAll();
    }

    @Override
    public Car addCar(CreateCarCommand commnand) {
        Car car = commnand.toCar();
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
}
