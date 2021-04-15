package pl.com.pslupski.letsDrive.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.pslupski.letsDrive.order.application.port.ModifyOrderUseCase;
import pl.com.pslupski.letsDrive.order.domain.Order;
import pl.com.pslupski.letsDrive.order.domain.OrderRepository;
import pl.com.pslupski.letsDrive.order.domain.OrderStatus;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ModifyOrderService implements ModifyOrderUseCase {
    private final OrderRepository repository;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {
        Order order = Order.builder()
                .recipient(command.getRecipient())
                .items(command.getItems())
                .build();
        Order savedOrder = repository.save(order);
        return PlaceOrderResponse.success(savedOrder.getId());
    }

    @Override
    public UpdateStatusResponse updateOrderStatus(Long id, OrderStatus status) {
        return repository.findById(id)
                .map(order -> {
                    order.setStatus(status);
                    Order updatedOrder = repository.save(order);
                    return UpdateStatusResponse.success(updatedOrder.getId());
                }).orElseGet(() -> new UpdateStatusResponse(false, id,
                        Collections.singletonList("Order NOT found with ID: " + id)));
    }

    @Override
    public void removeById(Long id) {
        repository.removeById(id);
    }
}
