package pl.com.pslupski.letsDrive.order.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Order {
    Long id;
    List<OrderItem> items;
    Recipient recipient;
    LocalDateTime createdAt;
    @Builder.Default
    OrderStatus status = OrderStatus.NEW;
}
