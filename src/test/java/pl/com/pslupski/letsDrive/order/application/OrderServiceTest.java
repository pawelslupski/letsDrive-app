package pl.com.pslupski.letsDrive.order.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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
import pl.com.pslupski.letsDrive.order.domain.Delivery;
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
                .delivery(Delivery.COURIER)
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
        String recipientEmail = "marek@example.org";
        Long orderId = placedOrder(carItem1.getId(), 17, recipientEmail);
        Assertions.assertEquals(3L, getAvailableCopiesOf(carItem1));
        // When
        // TODO fix on security module
        UpdateStatusCommand updateStatusCommand = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, recipientEmail);
        orderService.updateOrderStatus(updateStatusCommand);
        // Then
        Assertions.assertEquals(20L, getAvailableCopiesOf(carItem1));
        Assertions.assertEquals(OrderStatus.CANCELED, queryOrderUseCase.findById(orderId).get().getStatus());
    }

    @Disabled("todo")
    public void userCannotRevokePaidOrder() { }

    @Disabled("todo")
    public void userCannotRevokeShippedOrder() { }

    @Disabled("todo")
    public void userCannotOrderNoExistingBooks() { }

    @Disabled("todo")
    public void userCannotOrderNativeNumberOfBooks() { }

    private Long placedOrder(Long carItemId, int copies, String recipientEmail) {
        Recipient recipient = Recipient.builder().email(recipientEmail).build();
        PlaceOrderCommand orderCommand = PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new OrderItemCommand(carItemId, copies))
                .delivery(Delivery.COURIER)
                .build();
        PlaceOrderResponse response = orderService.placeOrder(orderCommand);
        return response.getOrderId();
    }

    private Long placedOrder(Long carItemId, int copies) {
        return placedOrder(carItemId, copies, "szpslupski@o2.pl");
    }

    @Test
    public void userCanOrderMoreCarItemsThanAvailableThrowsException() {
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

    @Test
    public void userCannotRevokeOtherUsersOrder() {
        // Given
        CarItem carItem1 = carItemJpaRepository.save(new CarItem("KZ2220T", new BigDecimal("124.67"),
                Category.CAR_PARTS, SubCategory.BRAKES, 20L));
        String recipientEmail = "baska@example.org";
        Long orderId = placedOrder(carItem1.getId(), 3, recipientEmail);
        Assertions.assertEquals(17L, getAvailableCopiesOf(carItem1));
        // When
        UpdateStatusCommand updateStatusCommand = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, "brianek@o2.pl");
        orderService.updateOrderStatus(updateStatusCommand);
        // Then
        assertEquals(17L, getAvailableCopiesOf(carItem1));
        assertEquals(OrderStatus.NEW, queryOrderUseCase.findById(orderId).get().getStatus());
    }

    @Test
    public void adminCanRevokeOtherUsersOrder() {
        // Given
        CarItem carItem1 = carItemJpaRepository.save(new CarItem("KZ2220T", new BigDecimal("124.67"),
                Category.CAR_PARTS, SubCategory.BRAKES, 20L));
        String adminEmail = "admin@example.org";
        Long orderId = placedOrder(carItem1.getId(), 6, adminEmail);
        Assertions.assertEquals(14L, getAvailableCopiesOf(carItem1));
        // When
        UpdateStatusCommand updateStatusCommand = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, adminEmail);
        orderService.updateOrderStatus(updateStatusCommand);
        // Then
        assertEquals(20L, getAvailableCopiesOf(carItem1));
        assertEquals(OrderStatus.CANCELED, queryOrderUseCase.findById(orderId).get().getStatus());
    }

    @Test
    public void adminCanMarkOrderAsPaid() {
        // Given
        CarItem carItem1 = carItemJpaRepository.save(new CarItem("KZ2220T", new BigDecimal("124.67"),
                Category.CAR_PARTS, SubCategory.BRAKES, 20L));
        String adminEmail = "admin@example.org";
        Long orderId = placedOrder(carItem1.getId(), 15, adminEmail);
        Assertions.assertEquals(5L, getAvailableCopiesOf(carItem1));
        // When
        // TODO fix on security module
        UpdateStatusCommand updateStatusCommand = new UpdateStatusCommand(orderId, OrderStatus.PAID, adminEmail);
        orderService.updateOrderStatus(updateStatusCommand);
        // Then
        Assertions.assertEquals(5L, getAvailableCopiesOf(carItem1));
        Assertions.assertEquals(OrderStatus.PAID, queryOrderUseCase.findById(orderId).get().getStatus());
    }

    private Long getAvailableCopiesOf(CarItem carItem) {
        return carItemUseCase.findById(carItem.getId()).get().getAvailable();
    }

    @Test
    public void shippingCostsAreAddedToTotalOrder() {
        // Given
        CarItem carItem = givenCarItem(30L, "29.90");
        // When
        Long orderId = placedOrder(carItem.getId(), 1);
        // Then
        assertEquals("39.80", orderOf(orderId).getFinalPrice().toPlainString());
    }

    private FullOrder orderOf(Long orderId) {
        return queryOrderUseCase.findById(orderId).get();
    }

    private CarItem givenCarItem(long available, String price) {
        return carItemJpaRepository.save(new CarItem("YU2550M", new BigDecimal(price),
                Category.OIL, SubCategory.COOLING_AND_HEATING, available));
    }
}
