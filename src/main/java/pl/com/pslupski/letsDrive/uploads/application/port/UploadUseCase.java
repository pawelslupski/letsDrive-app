package pl.com.pslupski.letsDrive.uploads.application.port;

import lombok.Value;
import pl.com.pslupski.letsDrive.uploads.domain.Upload;

import java.util.Optional;

public interface UploadUseCase {

    Upload save(SaveUploadCommand command);

    Optional<Upload> getById(String id);

    void removeById(String id);

    @Value
    class SaveUploadCommand {
        String fileName;
        byte[] file;
        String contentType;
    }
}