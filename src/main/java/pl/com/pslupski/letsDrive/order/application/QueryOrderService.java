package pl.com.pslupski.letsDrive.order.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.pslupski.letsDrive.catalog.carItem.db.CarItemJpaRepository;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.order.application.port.QueryOrderUseCase;
import pl.com.pslupski.letsDrive.order.db.OrderJpaRepository;
import pl.com.pslupski.letsDrive.order.domain.Order;
import pl.com.pslupski.letsDrive.order.domain.OrderItem;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QueryOrderService implements QueryOrderUseCase {
    private final OrderJpaRepository repository;
    private final CarItemJpaRepository itemRepository;

    @Override
    public List<FullOrder> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toFullOrder)
                .collect(Collectors.toList());
    }

    private FullOrder toFullOrder(Order order) {
        List<FullOrderItem> fullOrderItems = toFullOrderItems(order.getItems());
        return new FullOrder(
                order.getId(),
                fullOrderItems,
                order.getStatus(),
                order.getRecipient(),
                order.getCreatedAt()
        );
    }

    private List<FullOrderItem> toFullOrderItems(List<OrderItem> items) {
        return items.stream()
                .map(item -> {
                    CarItem carItem = itemRepository
                            .findById(item.getCarItemId())
                            .orElseThrow(() -> new IllegalStateException("Unable to find the item with ID: " + item.getCarItemId()));
                    return new FullOrderItem(carItem, item.getQuantity());
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FullOrder> findById(Long id) {
        return repository.findById(id).map(this::toFullOrder);
    }
}
