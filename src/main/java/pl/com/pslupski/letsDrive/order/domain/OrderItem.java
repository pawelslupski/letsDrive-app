package pl.com.pslupski.letsDrive.order.domain;

import lombok.Value;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;

@Value
public class OrderItem {
    CarItem item;
    int quantity;
}
