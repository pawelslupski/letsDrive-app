package pl.com.pslupski.letsDrive.order.application.price;

import pl.com.pslupski.letsDrive.order.domain.Order;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculate(Order order);
}
