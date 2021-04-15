package pl.com.pslupski.letsDrive.order.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Recipient {
    String firstname;
    String lastname;
    String street;
    String zipCode;
    String city;
    String email;
}
