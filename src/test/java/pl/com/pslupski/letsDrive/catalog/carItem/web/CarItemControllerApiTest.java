package pl.com.pslupski.letsDrive.catalog.carItem.web;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import pl.com.pslupski.letsDrive.catalog.carItem.application.port.CarItemUseCase;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.Category;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.SubCategory;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CarItemControllerApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;
    @MockBean
    CarItemUseCase carItemUseCase;

    @Test
    public void shouldGetAllCarItems() {
        // Given
        CarItem carItem1 = new CarItem("KZ2220T", new BigDecimal("124.67"), Category.CAR_PARTS, SubCategory.BRAKES, 20L);
        CarItem carItem2 = new CarItem("YI7821T", new BigDecimal("93.20"), Category.CAR_PARTS, SubCategory.ENGINE_PARTS, 25L);
        Mockito.when(carItemUseCase.findAll()).thenReturn(List.of(carItem1, carItem2));
        ParameterizedTypeReference<List<CarItem>> type = new ParameterizedTypeReference<>(){};
        // When
        String url = "http://localhost:" + port + "/items";
        RequestEntity<Void> requestEntity = RequestEntity.get(URI.create(url)).build();
        ResponseEntity<List<CarItem>> responseEntity = restTemplate.exchange(requestEntity, type);
        // Then
        assertEquals(2, Objects.requireNonNull(responseEntity.getBody()).size());
    }
}