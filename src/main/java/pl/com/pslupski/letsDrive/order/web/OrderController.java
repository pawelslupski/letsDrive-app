package pl.com.pslupski.letsDrive.order.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.pslupski.letsDrive.order.application.port.PlaceOrderUseCase;
import pl.com.pslupski.letsDrive.order.application.port.PlaceOrderUseCase.PlaceOrderCommand;
import pl.com.pslupski.letsDrive.order.application.port.QueryOrderUseCase;
import pl.com.pslupski.letsDrive.order.domain.Order;
import pl.com.pslupski.letsDrive.order.domain.OrderItem;
import pl.com.pslupski.letsDrive.order.domain.OrderStatus;
import pl.com.pslupski.letsDrive.order.domain.Recipient;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static pl.com.pslupski.letsDrive.order.application.port.PlaceOrderUseCase.*;

@RequestMapping("/orders")
@RestController
@AllArgsConstructor
public class OrderController {
    private final QueryOrderUseCase queryOrderUseCase;
    private final PlaceOrderUseCase placeOrderUseCase;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<Order> getAll() {
        return new ArrayList<>(queryOrderUseCase.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queryOrderUseCase.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> placeOrder(@Valid @RequestBody RestOrderCommand command) {
        PlaceOrderResponse response = placeOrderUseCase.placeOrder(command.toPlaceOrderCommand());
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
        URI uri = createdOrderUri(response);
        return ResponseEntity.created(uri).build();
    }

    private URI createdOrderUri(PlaceOrderResponse response) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/" + response.getOrderId().toString()).build().toUri();
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateBook(@PathVariable Long id, @RequestBody RestOrderCommand command) {
        UpdateOrderResponse response = placeOrderUseCase.updateOrder(command.toUpdateOrderCommand(id));
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        queryOrderUseCase.removeById(id);
    }

    @Data
    private static class RestOrderCommand {
        @NotNull(message = "Please provide some order items")
        List<OrderItem> items;
        @NotNull(message = "Please provide a recipient")
        private Recipient recipient;
        @NotNull(message = "Status is empty, default status will be used!")
        private OrderStatus status;

        PlaceOrderCommand toPlaceOrderCommand() {
            return new PlaceOrderCommand(items, recipient);
        }

        UpdateOrderCommand toUpdateOrderCommand(Long id) {
            return new UpdateOrderCommand(id, status);
        }
    }
}
