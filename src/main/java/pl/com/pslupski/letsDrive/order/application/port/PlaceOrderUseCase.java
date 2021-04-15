package pl.com.pslupski.letsDrive.order.application.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import pl.com.pslupski.letsDrive.order.domain.Order;
import pl.com.pslupski.letsDrive.order.domain.OrderItem;
import pl.com.pslupski.letsDrive.order.domain.OrderStatus;
import pl.com.pslupski.letsDrive.order.domain.Recipient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface PlaceOrderUseCase {

    PlaceOrderResponse placeOrder(PlaceOrderCommand command);
    UpdateOrderResponse updateOrder(UpdateOrderCommand command);

    @Builder
    @Value
    @AllArgsConstructor
    class PlaceOrderCommand {
        @Singular
        List<OrderItem> items;
        Recipient recipient;
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
    class UpdateOrderResponse {
        boolean success;
        Long orderId;
        List<String> errors;

        public static UpdateOrderResponse success(Long orderId) {
            return new UpdateOrderResponse(true, orderId, Collections.emptyList());
        }
    }
}
