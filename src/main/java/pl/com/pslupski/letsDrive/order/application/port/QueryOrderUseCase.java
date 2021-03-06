package pl.com.pslupski.letsDrive.order.application.port;

import lombok.Value;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.order.domain.OrderItem;
import pl.com.pslupski.letsDrive.order.domain.OrderStatus;
import pl.com.pslupski.letsDrive.order.domain.Recipient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface QueryOrderUseCase {

    List<FullOrder> findAll();

    Optional<FullOrder> findById(Long id);

    @Value
    class FullOrder {
        Long id;
        Set<OrderItem> items;
        OrderStatus status;
        Recipient recipient;
        LocalDateTime createdAt;

        public BigDecimal totalPrice() {
            return items.stream()
                    .map(item -> item.getCarItem().getPrice().multiply(new BigDecimal(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}
