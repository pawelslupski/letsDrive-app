package pl.com.pslupski.letsDrive.order.application.port;

import pl.com.pslupski.letsDrive.order.application.FullOrder;

import java.util.List;
import java.util.Optional;

public interface QueryOrderUseCase {

    List<FullOrder> findAll();

    Optional<FullOrder> findById(Long id);

}