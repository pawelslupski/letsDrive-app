package pl.com.pslupski.letsDrive.order.application;

import lombok.Value;
import pl.com.pslupski.letsDrive.order.domain.OrderItem;
import pl.com.pslupski.letsDrive.order.domain.OrderStatus;
import pl.com.pslupski.letsDrive.order.domain.Recipient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Value
public
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