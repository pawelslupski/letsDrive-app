package pl.com.pslupski.letsDrive.order.application.port;

import pl.com.pslupski.letsDrive.order.domain.Order;

import java.util.List;
import java.util.Optional;

public interface QueryOrderUseCase {

    List<Order> findAll();

    Optional<Order> findById(Long id);

    void removeById(Long id);
}
