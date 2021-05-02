package pl.com.pslupski.letsDrive.uploads.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.pslupski.letsDrive.uploads.application.port.UploadUseCase;
import pl.com.pslupski.letsDrive.uploads.db.UploadJpaRepository;
import pl.com.pslupski.letsDrive.uploads.domain.Upload;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UploadService implements UploadUseCase {
    private final UploadJpaRepository repository;

    @Override
    public Upload save(SaveUploadCommand command) {
        Upload upload = new Upload(
                command.getFileName(),
                command.getContentType(),
                command.getFile());
        repository.save(upload);
        System.out.println("Upload saved: " + upload.getFileName() + " with id: " + upload.getId());
        return upload;
    }

    @Override
    public Optional<Upload> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }
}
