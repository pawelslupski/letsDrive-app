package pl.com.pslupski.letsDrive.catalog.carItem.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.pslupski.letsDrive.catalog.carItem.application.port.CarItemUseCase;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItemRepository;
import pl.com.pslupski.letsDrive.uploads.application.port.UploadUseCase;
import pl.com.pslupski.letsDrive.uploads.domain.Upload;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static pl.com.pslupski.letsDrive.uploads.application.port.UploadUseCase.*;

@Service
@AllArgsConstructor
public class CarItemService implements CarItemUseCase {
    private final CarItemRepository repository;
    private final UploadUseCase upload;

    @Override
    public List<CarItem> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<CarItem> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public CarItem addCarItem(CreateCarItemCommand command) {
        CarItem carItem = command.toCarItem();
        return repository.save(carItem);
    }

    @Override
    public UpdateCarItemResponse updateCarItem(UpdateCarItemCommand command) {
        return repository.findById(command.getId())
                .map(carItem -> {
                    CarItem updatedCarItem = command.updateFields(carItem);
                    repository.save(updatedCarItem);
                    return UpdateCarItemResponse.SUCCESS;
                }).orElseGet(() -> new UpdateCarItemResponse(false,
                        Collections.singletonList("Car item not found with id: " + command.getId())));
    }

    @Override
    public void removeById(Long id) {
        repository.removeById(id);
    }

    @Override
    public void updateCarItemImage(UpdateCarItemImageCommand command) {
        repository.findById(command.getId())
                .ifPresent(carItem -> {
                    Upload savedUpload = upload.save(new SaveUploadCommand(command.getFileName(),
                            command.getFile(), command.getContentType()));
                    carItem.setImageId(savedUpload.getId());
                    repository.save(carItem);
                });
    }

    @Override
    public void removeCarItemImage(Long id) {
        repository.findById(id)
                .ifPresent(carItem -> {
                    if (carItem.getImageId() != null) {
                        upload.removeById(carItem.getImageId());
                    }
                    carItem.setImageId(null);
                    repository.save(carItem);
                });
    }
}
