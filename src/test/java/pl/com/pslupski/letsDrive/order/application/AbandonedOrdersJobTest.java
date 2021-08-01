package pl.com.pslupski.letsDrive.order.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import pl.com.pslupski.letsDrive.catalog.carItem.application.port.CarItemUseCase;
import pl.com.pslupski.letsDrive.catalog.carItem.db.CarItemJpaRepository;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.Category;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.SubCategory;
import pl.com.pslupski.letsDrive.clock.Clock;
import pl.com.pslupski.letsDrive.order.application.port.ModifyOrderUseCase;
import pl.com.pslupski.letsDrive.order.application.port.QueryOrderUseCase;
import pl.com.pslupski.letsDrive.order.domain.Delivery;
import pl.com.pslupski.letsDrive.order.domain.OrderStatus;
import pl.com.pslupski.letsDrive.order.domain.Recipient;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        properties = "app.orders.payment-period=1H"
)
@AutoConfigureTestDatabase
class AbandonedOrdersJobTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public Clock.Fake clock() {
            return new Clock.Fake();
        }
    }

    @Autowired
    AbandonedOrdersJob ordersJob;
    @Autowired
    ModifyOrderService orderService;
    @Autowired
    CarItemJpaRepository carItemRepository;
    @Autowired
    QueryOrderUseCase queryOrderUseCase;
    @Autowired
    CarItemUseCase carItemUseCase;
    @Autowired
    Clock.Fake clockFake;

    @Test
    public void shouldMarkOrdersAsAbandoned() {
        // Given - orders
        CarItem carItem = carItemRepository.save(new CarItem("KZ2220T", new BigDecimal("124.67"),
                Category.CAR_PARTS, SubCategory.BRAKES, 20L));
        Long orderId = placedOrder(carItem.getId(), 6);
        // When - run
        clockFake.tick(Duration.ofHours(2));
        ordersJob.run();

        // Then - status changed
        Assertions.assertEquals(OrderStatus.ABANDONED, queryOrderUseCase.findById(orderId).get().getStatus());
        assertEquals(20L, carItemUseCase.findById(carItem.getId()).get().getAvailable());
    }

    private Long placedOrder(Long carItemId, int copies) {
        Recipient recipient = Recipient.builder().email("maciek@example.org").build();
        ModifyOrderUseCase.PlaceOrderCommand orderCommand = ModifyOrderUseCase.PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new ModifyOrderUseCase.OrderItemCommand(carItemId, copies))
                .build();
        ModifyOrderUseCase.PlaceOrderResponse response = orderService.placeOrder(orderCommand);
        return response.getOrderId();
    }
}