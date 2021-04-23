package pl.com.pslupski.letsDrive.order.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue
    private Long id;
    private Long carItemId;
    private int quantity;

    public OrderItem(Long carItemId, int quantity) {
        this.carItemId = carItemId;
        this.quantity = quantity;
    }
}
