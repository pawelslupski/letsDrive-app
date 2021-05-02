package pl.com.pslupski.letsDrive.order.application.port;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.pslupski.letsDrive.order.db.OrderJpaRepository;
import pl.com.pslupski.letsDrive.order.domain.Order;
import pl.com.pslupski.letsDrive.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
class AbandonedOrdersJob {
    private final OrderJpaRepository repository;
    private final ModifyOrderUseCase orderUseCase;

    @Transactional
    @Scheduled(fixedRate = 60_000)
    public void run() {
        LocalDateTime timestamp = LocalDateTime.now().minusDays(2);
        List<Order> orders = repository.findByStatusAndCreatedAtIsLessThanEqual(OrderStatus.NEW, timestamp);
        System.out.println("Found orders to be abandoned: " + orders);
        orders.forEach(order -> orderUseCase.updateOrderStatus(order.getId(), OrderStatus.ABANDONED));
    }
}
