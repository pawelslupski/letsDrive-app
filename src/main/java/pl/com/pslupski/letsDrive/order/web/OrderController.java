package pl.com.pslupski.letsDrive.order.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.pslupski.letsDrive.order.application.port.ModifyOrderUseCase;
import pl.com.pslupski.letsDrive.order.application.port.ModifyOrderUseCase.PlaceOrderCommand;
import pl.com.pslupski.letsDrive.order.application.port.QueryOrderUseCase;
import pl.com.pslupski.letsDrive.order.application.port.QueryOrderUseCase.FullOrder;
import pl.com.pslupski.letsDrive.order.domain.OrderStatus;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static pl.com.pslupski.letsDrive.order.application.port.ModifyOrderUseCase.PlaceOrderResponse;
import static pl.com.pslupski.letsDrive.order.application.port.ModifyOrderUseCase.UpdateStatusResponse;

@RequestMapping("/orders")
@RestController
@AllArgsConstructor
public class OrderController {
    private final ModifyOrderUseCase modifyOrderUseCase;
    private final QueryOrderUseCase queryOrderUseCase;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<FullOrder> getAll() {
        return queryOrderUseCase.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        return queryOrderUseCase.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> placeOrder(@Valid @RequestBody PlaceOrderCommand command) {
        PlaceOrderResponse response = modifyOrderUseCase.placeOrder(command);
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
    public void updateOrderStatus(@PathVariable Long id, @RequestBody UpdateStatusCommand command) {
        OrderStatus orderStatus = OrderStatus.parseString(command.status)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Unknown status: " + command.status));
        UpdateStatusResponse response = modifyOrderUseCase.updateOrderStatus(id, orderStatus);
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        modifyOrderUseCase.removeById(id);
    }

    @Data
    static class UpdateStatusCommand {
        String status;
    }
}
