package pl.com.pslupski.letsDrive.order.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Recipient {
    String firstname;
    String lastname;
    String street;
    String zipCode;
    String city;
    String email;
    String phone;
}
