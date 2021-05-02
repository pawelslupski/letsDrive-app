package pl.com.pslupski.letsDrive.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.pslupski.letsDrive.catalog.carItem.db.CarItemJpaRepository;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.order.application.port.ModifyOrderUseCase;
import pl.com.pslupski.letsDrive.order.db.OrderJpaRepository;
import pl.com.pslupski.letsDrive.order.db.RecipientJPaRepository;
import pl.com.pslupski.letsDrive.order.domain.Order;
import pl.com.pslupski.letsDrive.order.domain.OrderItem;
import pl.com.pslupski.letsDrive.order.domain.OrderStatus;
import pl.com.pslupski.letsDrive.order.domain.Recipient;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ModifyOrderService implements ModifyOrderUseCase {
    private final OrderJpaRepository repository;
    private final CarItemJpaRepository carItemRepository;
    private final RecipientJPaRepository recipientRepository;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {
        Set<OrderItem> items = command.getItems().stream()
                .map(this::toOrderItem)
                .collect(Collectors.toSet());
        Order order = Order.builder()
                .recipient(getOrCreateRecipient(command.getRecipient()))
                .items(items)
                .build();
        Order savedOrder = repository.save(order);
        carItemRepository.saveAll(updateItemsQuantity(items));
        return PlaceOrderResponse.success(savedOrder.getId());
    }

    private Recipient getOrCreateRecipient(Recipient recipient) {
        return recipientRepository
                .findByEmailIgnoreCase(recipient.getEmail())
                .orElse(recipient);
    }

    private OrderItem toOrderItem(OrderItemCommand command) {
        CarItem carItem = carItemRepository.getOne(command.getCarItemId());
        int quantity = command.getQuantity();
        if (carItem.getAvailable() >= quantity) {
            return new OrderItem(carItem, quantity);
        }
        throw new IllegalArgumentException("Too many copies of the part " + carItem.getId() + " requested: " +  quantity + " of " + carItem.getAvailable() + " available ");
    }

    @Override
    public UpdateStatusResponse updateOrderStatus(Long id, OrderStatus status) {
        return repository.findById(id)
                .map(order -> {
                    order.updateStatus(status);
                    Order updatedOrder = repository.save(order);
                    return UpdateStatusResponse.success(updatedOrder.getId());
                }).orElseGet(() -> new UpdateStatusResponse(false, id,
                        Collections.singletonList("Order NOT found with ID: " + id)));
    }

    private Set<CarItem> updateItemsQuantity(Set<OrderItem> items) {
        return items.stream()
                .map(orderItem -> {
                    CarItem carItem = orderItem.getCarItem();
                    carItem.setAvailable(carItem.getAvailable() - orderItem.getQuantity());
                    return carItem;
                }).collect(Collectors.toSet());
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }
}
