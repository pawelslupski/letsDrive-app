package pl.com.pslupski.letsDrive.order.domain;

import lombok.Value;

@Value
public class OrderItem {
    Long carItemId;
    int quantity;
}
