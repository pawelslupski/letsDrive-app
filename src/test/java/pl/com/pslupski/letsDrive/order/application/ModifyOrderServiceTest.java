package pl.com.pslupski.letsDrive.order.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import pl.com.pslupski.letsDrive.catalog.carItem.db.CarItemJpaRepository;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.Category;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.SubCategory;
import pl.com.pslupski.letsDrive.order.domain.Recipient;

import java.math.BigDecimal;

import static pl.com.pslupski.letsDrive.order.application.port.ModifyOrderUseCase.*;

@DataJpaTest
@Import({ModifyOrderService.class})
class ModifyOrderServiceTest {

    @Autowired
    CarItemJpaRepository carItemJpaRepository;
    @Autowired
    ModifyOrderService orderService;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void userCanPlaceOrder() {
        //Given
        CarItem carItem1 = carItemJpaRepository.save(new CarItem("KZ2220T", new BigDecimal("124.67"), Category.CAR_PARTS, SubCategory.BRAKES, 20L));
        CarItem carItem2 = carItemJpaRepository.save(new CarItem("YI7821T", new BigDecimal("93.20"), Category.CAR_PARTS, SubCategory.ENGINE_PARTS, 25L));
        Recipient recipient = Recipient.builder().email("szpslupski@o2.pl").build();
        PlaceOrderCommand orderCommand = PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new OrderItemCommand(carItem1.getId(), 10))
                .item(new OrderItemCommand(carItem2.getId(), 10))
                .build();
        //When
        PlaceOrderResponse response = orderService.placeOrder(orderCommand);
        //Then
        Assertions.assertTrue(response.isSuccess());
    }

}