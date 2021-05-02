package pl.com.pslupski.letsDrive.order.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.jpa.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderItem extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "car_item_id")
    private CarItem carItem;
    private int quantity;
}
