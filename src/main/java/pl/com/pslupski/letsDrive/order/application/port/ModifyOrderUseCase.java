package pl.com.pslupski.letsDrive.order.application.port;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import pl.com.pslupski.letsDrive.commons.Either;
import pl.com.pslupski.letsDrive.order.domain.Delivery;
import pl.com.pslupski.letsDrive.order.domain.Order;
import pl.com.pslupski.letsDrive.order.domain.OrderStatus;
import pl.com.pslupski.letsDrive.order.domain.Recipient;

import java.util.List;

public interface ModifyOrderUseCase {

    PlaceOrderResponse placeOrder(PlaceOrderCommand command);

    UpdateStatusResponse updateOrderStatus(UpdateStatusCommand command);

    void removeById(Long id);

    @Builder
    @Value
    @AllArgsConstructor
    class PlaceOrderCommand {
        @Singular
        List<OrderItemCommand> items;
        Recipient recipient;
        @Builder.Default
        Delivery delivery = Delivery.COURIER;
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
        User user;
    }

    class PlaceOrderResponse extends Either<String, Long> {
        public PlaceOrderResponse(boolean success, String left, Long right) {
            super(success, left, right);
        }

        public static PlaceOrderResponse success(Long orderId) {
            return new PlaceOrderResponse(true, null, orderId);
        }

        public static PlaceOrderResponse failure(String error) {
            return new PlaceOrderResponse(false, error, null);
        }
    }

    class UpdateStatusResponse extends Either<Error, OrderStatus> {
        public UpdateStatusResponse(boolean success, Error left, OrderStatus right) {
            super(success, left, right);
        }

        public static UpdateStatusResponse success(OrderStatus status) {
            return new UpdateStatusResponse(true, null, status);
        }

        public static UpdateStatusResponse failure(Error error) {
            return new UpdateStatusResponse(false, error, null);
        }
    }

    @AllArgsConstructor
    @Getter
    enum Error {
        NOT_FOUND(HttpStatus.NOT_FOUND), FORBIDDEN(HttpStatus.FORBIDDEN);

        private final HttpStatus status;
    }
}