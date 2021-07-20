package pl.com.pslupski.letsDrive.order.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.order.domain.OrderItem;
import pl.com.pslupski.letsDrive.order.domain.OrderStatus;
import pl.com.pslupski.letsDrive.order.domain.Recipient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class FullOrderTest {

    @Test
    public void calculatesTotalPriceOfEmptyOrder() {
        // Given
        FullOrder fullOrder = new FullOrder(
                1L,
                Collections.emptySet(),
                OrderStatus.NEW,
                Recipient.builder().build(),
                LocalDateTime.now());
        // When
        BigDecimal price = fullOrder.totalPrice();
        // Then
        Assertions.assertEquals(BigDecimal.ZERO, price);
    }

    @Test
    public void calculatesTotalPrice() {
        // Given
        CarItem carItem = new CarItem();
        carItem.setPrice(new BigDecimal("12.50"));
        CarItem carItem2 = new CarItem();
        carItem2.setPrice(new BigDecimal("16.00"));
        Set<OrderItem> items = new HashSet<>(
                Arrays.asList(
                        new OrderItem(carItem, 2),
                        new OrderItem(carItem2, 5)
                )
        );
        FullOrder fullOrder = new FullOrder(
                1L,
                items,
                OrderStatus.NEW,
                Recipient.builder().build(),
                LocalDateTime.now());
        // When
        BigDecimal price = fullOrder.totalPrice();
        // Then
        Assertions.assertEquals(new BigDecimal("105.00"), price);
    }
}