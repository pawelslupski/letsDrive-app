package pl.com.pslupski.letsDrive;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.com.pslupski.letsDrive.catalog.car.application.port.CarUseCase;
import pl.com.pslupski.letsDrive.catalog.car.domain.Car;
import pl.com.pslupski.letsDrive.catalog.carItem.application.port.CarItemUseCase;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.Category;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.SubCategory;
import pl.com.pslupski.letsDrive.order.application.port.ModifyOrderUseCase;
import pl.com.pslupski.letsDrive.order.domain.OrderItem;
import pl.com.pslupski.letsDrive.order.domain.Recipient;

import java.math.BigDecimal;
import java.util.Set;

import static pl.com.pslupski.letsDrive.catalog.car.application.port.CarUseCase.CreateCarCommand;
import static pl.com.pslupski.letsDrive.order.application.port.ModifyOrderUseCase.PlaceOrderCommand;

@Component
@AllArgsConstructor
public class ApplicationStartup implements CommandLineRunner {
    private final CarUseCase carCatalog;
    private final CarItemUseCase carItemCatalog;
    private final ModifyOrderUseCase placeOrder;

    @Override
    public void run(String... args) throws Exception {
        initData();
        placeOrder();
    }

    private void initData() {
        Car car = carCatalog.addCar(new CreateCarCommand("BMW", "X5", 2019, 4.4, "petrol"));
        Car car2 = carCatalog.addCar(new CreateCarCommand("Toyota",  "Prius", 2017, 1.5, "hybrid"));
        Car car3 = carCatalog.addCar(new CreateCarCommand("Volvo", "S60", 2014, 2.0, "petrol"));
        Car car4 = carCatalog.addCar(new CreateCarCommand("Fiat", "Punto", 2012, 1.2, "petrol"));
        Car car5 = carCatalog.addCar(new CreateCarCommand("Opel", "Mokka", 2017, 1.8, "diesel"));

        carItemCatalog.addCarItem(new CarItemUseCase.CreateCarItemCommand("KJ5646T", new BigDecimal("123.67"), Category.CAR_PARTS, SubCategory.BRAKES, Set.of(car.getId())));
        carItemCatalog.addCarItem(new CarItemUseCase.CreateCarItemCommand("KY5622I", new BigDecimal("255.45"), Category.CAR_PARTS, SubCategory.BRAKES, Set.of(car.getId(), car2.getId())));
        carItemCatalog.addCarItem(new CarItemUseCase.CreateCarItemCommand("YU5231L", new BigDecimal("1200.00"), Category.CAR_PARTS, SubCategory.COOLING_AND_HEATING, Set.of(car3.getId(), car4.getId(), car5.getId())));
    }

    private void placeOrder() {
        CarItem carItem = carItemCatalog.findByProductCode("KJ5646T")
                .orElseThrow(() -> new IllegalStateException("Cannot find the car item"));
        CarItem carItem2 = carItemCatalog.findByProductCode("YU5231L")
                .orElseThrow(() -> new IllegalStateException("Cannot find the car item"));

        Recipient recipient = Recipient
                .builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .phone("123-456-789")
                .street("Armii Krajowej 31")
                .city("Krakow")
                .zipCode("30-150")
                .email("jan@example.org")
                .build();

        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new OrderItem(carItem.getId(), 2))
                .item(new OrderItem(carItem2.getId(), 3))
                .build();

        placeOrder.placeOrder(command);
    }
}
