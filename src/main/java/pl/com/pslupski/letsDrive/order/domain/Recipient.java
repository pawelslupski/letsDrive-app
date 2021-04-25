package pl.com.pslupski.letsDrive.order.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Recipient {
    @Id
    @GeneratedValue
    private Long id;
    private String firstname;
    private String lastname;
    private String street;
    private String zipCode;
    private String city;
    private String email;
    private String phone;

    public Recipient(String firstname, String lastname, String street, String zipCode,
                     String city, String email, String phone) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
        this.email = email;
        this.phone = phone;
    }
}
