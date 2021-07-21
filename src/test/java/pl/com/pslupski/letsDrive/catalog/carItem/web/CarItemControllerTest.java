package pl.com.pslupski.letsDrive.catalog.carItem.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.com.pslupski.letsDrive.catalog.carItem.application.port.CarItemUseCase;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.Category;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.SubCategory;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CarItemController.class})
class CarItemControllerTest {

    @Autowired
    CarItemController carItemController;
    @MockBean
    CarItemUseCase carItemUseCase;

    @Test
    public void shouldGetAllCarItems() {
        // Given
        CarItem carItem1 = new CarItem("KZ2220T", new BigDecimal("124.67"), Category.CAR_PARTS, SubCategory.BRAKES, 20L);
        CarItem carItem2 = new CarItem("YI7821T", new BigDecimal("93.20"), Category.CAR_PARTS, SubCategory.ENGINE_PARTS, 25L);
        Mockito.when(carItemUseCase.findAll()).thenReturn(List.of(carItem1, carItem2));
        // When
        List<CarItem> all = carItemController.getAll(2);
        // Then
        assertEquals(2, all.size());
    }

}
