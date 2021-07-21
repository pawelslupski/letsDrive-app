package pl.com.pslupski.letsDrive.order.application.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import pl.com.pslupski.letsDrive.order.domain.Order;
import pl.com.pslupski.letsDrive.order.domain.OrderStatus;
import pl.com.pslupski.letsDrive.order.domain.Recipient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface ModifyOrderUseCase {

    PlaceOrderResponse placeOrder(PlaceOrderCommand command);

    UpdateStatusResponse updateOrderStatus(UpdateStatusCommand c);

    void removeById(Long id);

    @Builder
    @Value
    @AllArgsConstructor
    class PlaceOrderCommand {
        @Singular
        List<OrderItemCommand> items;
        Recipient recipient;
    }

    @Value
    class OrderItemCommand {
        Long carItemId;
        int quantity;
    }

    @Value
    class UpdateOrderCommand {
        Long orderId;
        OrderStatus status;

        public Order updateStatus(Order order) {
            if (status != null) {
                order.setStatus(status);
            }
            return order;
        }
    }

    @Value
    class UpdateStatusCommand {
        Long orderId;
        OrderStatus status;
        String email;
    }

    @Value
    class PlaceOrderResponse {
        boolean success;
        Long orderId;
        List<String> errors;

        public static PlaceOrderResponse success(Long orderId) {
            return new PlaceOrderResponse(true, orderId, Collections.emptyList());
        }

        public static PlaceOrderResponse failure(String... errors) {
            return new PlaceOrderResponse(false, null, Arrays.asList(errors));
        }
    }

    @Value
    class UpdateStatusResponse {
        boolean success;
        Long orderId;
        List<String> errors;

        public static UpdateStatusResponse success(Long orderId) {
            return new UpdateStatusResponse(true, orderId, Collections.emptyList());
        }
    }
}
