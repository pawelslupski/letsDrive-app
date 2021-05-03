package pl.com.pslupski.letsDrive.order.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.pslupski.letsDrive.order.domain.Order;
import pl.com.pslupski.letsDrive.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatusAndCreatedAtIsLessThanEqual(OrderStatus status, LocalDateTime time);
}
