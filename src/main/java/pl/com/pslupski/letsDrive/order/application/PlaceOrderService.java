package pl.com.pslupski.letsDrive.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.pslupski.letsDrive.order.application.port.PlaceOrderUseCase;
import pl.com.pslupski.letsDrive.order.domain.Order;
import pl.com.pslupski.letsDrive.order.domain.OrderRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class PlaceOrderService implements PlaceOrderUseCase {
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
    public UpdateOrderResponse updateOrder(UpdateOrderCommand command) {
        return repository.findById(command.getOrderId())
                .map(order -> {
                    Order updatedOrder = command.updateStatus(order);
                    Order savedOrder = repository.save(updatedOrder);
                    return UpdateOrderResponse.success(savedOrder.getId());
                }).orElseGet(() -> new UpdateOrderResponse(false, command.getOrderId(),
                        Collections.singletonList("Order NOT found with ID: " + command.getOrderId())));
    }
}
