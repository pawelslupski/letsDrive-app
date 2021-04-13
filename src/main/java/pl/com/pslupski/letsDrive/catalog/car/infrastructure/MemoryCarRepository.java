package pl.com.pslupski.letsDrive.catalog.car.infrastructure;

import org.springframework.stereotype.Repository;
import pl.com.pslupski.letsDrive.catalog.car.domain.Car;
import pl.com.pslupski.letsDrive.catalog.car.domain.CarRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryCarRepository implements CarRepository {
    private final Map<Long, Car> storage = new ConcurrentHashMap<>();
    private final AtomicLong ID_NEXT_VALUE = new AtomicLong(1L);

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Car save(Car car) {
        if (car.getId() != null) {
            storage.put(car.getId(), car);
        } else {
            long nextId = nextId();
            car.setId(nextId);
            storage.put(nextId, car);
        }
        return car;
    }

    @Override
    public Optional<Car> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void removeById(Long id) {
        storage.remove(id);
    }

    private long nextId() {
        return ID_NEXT_VALUE.getAndIncrement();
    }
}
