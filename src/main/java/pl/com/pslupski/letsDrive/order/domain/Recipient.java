package pl.com.pslupski.letsDrive.order.domain;

import lombok.*;
import pl.com.pslupski.letsDrive.jpa.BaseEntity;

import javax.persistence.Entity;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Recipient extends BaseEntity {
    private String firstname;
    private String lastname;
    private String street;
    private String zipCode;
    private String city;
    private String email;
    private String phone;
}
