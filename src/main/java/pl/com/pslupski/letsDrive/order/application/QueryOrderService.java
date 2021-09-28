package pl.com.pslupski.letsDrive.order.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.pslupski.letsDrive.order.application.port.QueryOrderUseCase;
import pl.com.pslupski.letsDrive.order.application.price.OrderPrice;
import pl.com.pslupski.letsDrive.order.application.price.PriceService;
import pl.com.pslupski.letsDrive.order.db.OrderJpaRepository;
import pl.com.pslupski.letsDrive.order.domain.Order;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QueryOrderService implements QueryOrderUseCase {
    private final OrderJpaRepository repository;
    private final PriceService priceService;

    @Override
    @Transactional
    public List<FullOrder> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toFullOrder)
                .collect(Collectors.toList());
    }

    private FullOrder toFullOrder(Order order) {
        OrderPrice orderPrice = priceService.calculatePrice(order);
        return new FullOrder(
                order.getId(),
                order.getItems(),
                order.getStatus(),
                order.getRecipient(),
                order.getCreatedAt(),
                orderPrice,
                orderPrice.finalPrice()
        );
    }

    @Override
    @Transactional
    public Optional<FullOrder> findById(Long id) {
        return repository.findById(id).map(this::toFullOrder);
    }
}
