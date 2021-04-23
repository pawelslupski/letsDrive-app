package pl.com.pslupski.letsDrive.uploads.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Upload {
    @Id
    @GeneratedValue
    Long id;
    String fileName;
    byte[] file;
    String contentType;
    LocalDateTime createdAt;

    public Upload(String fileName, String contentType, byte[] file) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.file = file;
    }
}
