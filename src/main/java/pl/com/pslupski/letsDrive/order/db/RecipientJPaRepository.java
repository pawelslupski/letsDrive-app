package pl.com.pslupski.letsDrive.order.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.pslupski.letsDrive.order.domain.Recipient;

import java.util.Optional;

public interface RecipientJPaRepository extends JpaRepository<Recipient, Long> {

    Optional<Recipient> findByEmailIgnoreCase(String email);
}
