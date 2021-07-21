package pl.com.pslupski.letsDrive.catalog.carItem.web;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.com.pslupski.letsDrive.catalog.carItem.application.port.CarItemUseCase;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.Category;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.SubCategory;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({CarItemController.class})
class CarItemControllerWebTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    CarItemUseCase carItemUseCase;

    @Test
    public void shouldGetAllCarParts() throws Exception {
        // Given
        CarItem carItem1 = new CarItem("KZ2220T", new BigDecimal("124.67"), Category.CAR_PARTS, SubCategory.BRAKES, 20L);
        CarItem carItem2 = new CarItem("YI7821T", new BigDecimal("93.20"), Category.CAR_PARTS, SubCategory.ENGINE_PARTS, 25L);
        Mockito.when(carItemUseCase.findAll()).thenReturn(List.of(carItem1, carItem2));
        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.get("/items"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
