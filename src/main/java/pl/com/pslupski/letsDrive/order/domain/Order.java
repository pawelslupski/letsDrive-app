package pl.com.pslupski.letsDrive.order.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.com.pslupski.letsDrive.jpa.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity {
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Set<OrderItem> items;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Recipient recipient;
    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status = OrderStatus.NEW;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public UpdateStatusResult updateStatus(OrderStatus newStatus) {
        UpdateStatusResult result = status.updateStatus(newStatus);
        this.status = result.getNewStatus();
        return result;
    }
}
