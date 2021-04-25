package pl.com.pslupski.letsDrive.uploads.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.pslupski.letsDrive.uploads.domain.Upload;

public interface UploadJpaRepository extends JpaRepository<Upload, Long> {
}
