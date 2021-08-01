package pl.com.pslupski.letsDrive.order.application.price;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.order.domain.Order;
import pl.com.pslupski.letsDrive.order.domain.OrderItem;

import java.math.BigDecimal;

class PriceServiceTest {
    PriceService priceService = new PriceService();

    @Test
    public void calculatesTotalPriceOfEmptyOrder() {
        // Given
        Order order = Order
                .builder()
                .build();
        // When
        OrderPrice price = priceService.calculatePrice(order);
        // Then
        Assertions.assertEquals(BigDecimal.ZERO, price.finalPrice());
    }

    @Test
    public void calculatesTotalPrice() {
        // Given
        CarItem carItem = new CarItem();
        carItem.setPrice(new BigDecimal("12.50"));
        CarItem carItem2 = new CarItem();
        carItem2.setPrice(new BigDecimal("16.00"));
        Order order = Order.builder()
                .item(new OrderItem(carItem, 3))
                .item(new OrderItem(carItem2, 5))
                .build();
        // When
        OrderPrice price = priceService.calculatePrice(order);
        // Then
        Assertions.assertEquals(new BigDecimal("117.50"), price.getItemsPrice());
        Assertions.assertEquals(new BigDecimal("117.50"), price.finalPrice());
    }
}