package pl.com.pslupski.letsDrive.order.application.price;

import pl.com.pslupski.letsDrive.order.domain.Order;
import pl.com.pslupski.letsDrive.order.domain.OrderItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public class TotalPriceDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal calculate(Order order) {
        if (isGreaterOrEquals(order, 400)) {
            return lowestCarItemPrice(order.getItems());
        } else if (isGreaterOrEquals(order, 200)) {
            BigDecimal lowestCarItemPrice = lowestCarItemPrice(order.getItems());
            return lowestCarItemPrice.divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal lowestCarItemPrice(Set<OrderItem> items) {
        return items.stream()
                .map(x -> x.getCarItem().getPrice())
                .sorted()
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }

    private boolean isGreaterOrEquals(Order order, int value) {
        return order.getItemsPrice().compareTo(BigDecimal.valueOf(value)) >= 0;
    }
}