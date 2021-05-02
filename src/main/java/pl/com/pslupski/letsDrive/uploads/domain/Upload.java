package pl.com.pslupski.letsDrive.uploads.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.com.pslupski.letsDrive.jpa.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Upload extends BaseEntity {
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
