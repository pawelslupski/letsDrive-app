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
import pl.com.pslupski.letsDrive.order.domain.Recipient;
import pl.com.pslupski.letsDrive.order.domain.UpdateStatusResult;
import pl.com.pslupski.letsDrive.security.UserSecurity;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ModifyOrderService implements ModifyOrderUseCase {
    private final OrderJpaRepository repository;
    private final CarItemJpaRepository carItemRepository;
    private final RecipientJPaRepository recipientRepository;
    private final UserSecurity userSecurity;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {
        Set<OrderItem> items = command
                .getItems()
                .stream()
                .map(this::toOrderItem)
                .collect(Collectors.toSet());
        Order order = Order.builder()
                .recipient(getOrCreateRecipient(command.getRecipient()))
                .delivery(command.getDelivery())
                .items(items)
                .build();
        Order savedOrder = repository.save(order);
        carItemRepository.saveAll(reduceItemsQty(items));
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
    @Transactional
    public UpdateStatusResponse updateOrderStatus(UpdateStatusCommand command) {
        return repository.findById(command.getOrderId())
                .map(order -> {
                    if (userSecurity.isOwnerOrAdmin(command.getUser(), order.getRecipient().getEmail())) {
                        UpdateStatusResult updateStatusResult = order.updateStatus(command.getStatus());
                        if (updateStatusResult.isRevoked()) {
                            carItemRepository.saveAll(revokeItemsQty(order.getItems()));
                        }
                        repository.save(order);
                        return UpdateStatusResponse.success(order.getStatus());
                    }
                    return UpdateStatusResponse.failure(Error.FORBIDDEN);
                }).orElseGet(() -> UpdateStatusResponse.failure(Error.NOT_FOUND));
    }

    private Set<CarItem> reduceItemsQty(Set<OrderItem> items) {
        return items.stream()
                .map(orderItem -> {
                    CarItem carItem = orderItem.getCarItem();
                    carItem.setAvailable(carItem.getAvailable() - orderItem.getQuantity());
                    return carItem;
                }).collect(Collectors.toSet());
    }

    private Set<CarItem> revokeItemsQty(Set<OrderItem> items) {
        return items.stream()
                .map(orderItem -> {
                    CarItem carItem = orderItem.getCarItem();
                    carItem.setAvailable(carItem.getAvailable() + orderItem.getQuantity());
                    return carItem;
                }).collect(Collectors.toSet());
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }
}