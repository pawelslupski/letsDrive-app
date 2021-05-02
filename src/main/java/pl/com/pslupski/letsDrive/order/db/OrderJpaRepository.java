package pl.com.pslupski.letsDrive.order.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.pslupski.letsDrive.order.domain.Order;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
