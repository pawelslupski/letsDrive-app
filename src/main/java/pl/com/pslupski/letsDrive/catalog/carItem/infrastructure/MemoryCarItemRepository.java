package pl.com.pslupski.letsDrive.catalog.carItem.infrastructure;

import org.springframework.stereotype.Repository;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryCarItemRepository implements CarItemRepository {
    private final Map<Long, CarItem> storage = new ConcurrentHashMap<>();
    private final AtomicLong ID_NEXT_VALUE = new AtomicLong(1L);

    @Override
    public List<CarItem> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public CarItem save(CarItem item) {
        if (item.getId() != null) {
            storage.put(item.getId(), item);
        } else {
            long nextId = nextId();
            item.setId(nextId);
            storage.put(nextId, item);
        }
        return item;
    }

    @Override
    public Optional<CarItem> findById(Long id) {
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
