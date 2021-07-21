package pl.com.pslupski.letsDrive.order.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.com.pslupski.letsDrive.catalog.carItem.application.port.CarItemUseCase;
import pl.com.pslupski.letsDrive.catalog.carItem.db.CarItemJpaRepository;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.Category;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.SubCategory;
import pl.com.pslupski.letsDrive.order.application.port.QueryOrderUseCase;
import pl.com.pslupski.letsDrive.order.domain.OrderStatus;
import pl.com.pslupski.letsDrive.order.domain.Recipient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.com.pslupski.letsDrive.order.application.port.ModifyOrderUseCase.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderServiceTest {

    @Autowired
    CarItemJpaRepository carItemJpaRepository;
    @Autowired
    CarItemUseCase carItemUseCase;
    @Autowired
    ModifyOrderService orderService;
    @Autowired
    QueryOrderUseCase queryOrderUseCase;

    @Test
    public void userCanPlaceOrder() {
        //Given
        CarItem carItem1 = carItemJpaRepository.save(new CarItem("KZ2220T", new BigDecimal("124.67"),
                Category.CAR_PARTS, SubCategory.BRAKES, 20L));
        CarItem carItem2 = carItemJpaRepository.save(new CarItem("YI7821T", new BigDecimal("93.20"),
                Category.CAR_PARTS, SubCategory.ENGINE_PARTS, 25L));
        Recipient recipient = Recipient.builder().email("szpslupski@o2.pl").build();
        PlaceOrderCommand orderCommand = PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new OrderItemCommand(carItem1.getId(), 8))
                .item(new OrderItemCommand(carItem2.getId(), 10))
                .build();
        //When
        PlaceOrderResponse response = orderService.placeOrder(orderCommand);
        //Then
        Assertions.assertTrue(response.isSuccess());
        assertEquals(12L, getAvailableCopiesOf(carItem1));
        assertEquals(15L, carItemUseCase.findById(carItem2.getId()).get().getAvailable());
    }

    @Test
    public void userCanRevokeOrder() {
        // Given
        CarItem carItem1 = carItemJpaRepository.save(new CarItem("KZ2220T", new BigDecimal("124.67"),
                Category.CAR_PARTS, SubCategory.BRAKES, 20L));
        Long orderId = placedOrder(carItem1.getId(), 17);
        Assertions.assertEquals(3L, getAvailableCopiesOf(carItem1));
        // When
        orderService.updateOrderStatus(orderId, OrderStatus.CANCELED);
        // Then
        Assertions.assertEquals(20L, getAvailableCopiesOf(carItem1));
        Assertions.assertEquals(OrderStatus.CANCELED, queryOrderUseCase.findById(orderId).get().getStatus());
    }

    private Long placedOrder(Long carItemId, int copies) {
        Recipient recipient = Recipient.builder().email("szpslupski@o2.pl").build();
        PlaceOrderCommand orderCommand = PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new OrderItemCommand(carItemId, copies))
                .build();
        PlaceOrderResponse response = orderService.placeOrder(orderCommand);
        return response.getOrderId();
    }

    @Test
    public void userCanOrderMoreCarItemsThanAvailable() {
        //Given
        CarItem carItem1 = carItemJpaRepository.save(new CarItem("KZ2220T", new BigDecimal("124.67"),
                Category.CAR_PARTS, SubCategory.BRAKES, 20L));
        Recipient recipient = Recipient.builder().email("szpslupski@o2.pl").build();
        PlaceOrderCommand orderCommand = PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new OrderItemCommand(carItem1.getId(), 24))
                .build();
        //When
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            orderService.placeOrder(orderCommand);
        });
        //Then
        Assertions.assertTrue(exception.getMessage()
                .contains("Too many copies of the part " + carItem1.getId() + " requested"));
    }

    private Long getAvailableCopiesOf(CarItem carItem) {
        return carItemUseCase.findById(carItem.getId()).get().getAvailable();
    }
}
