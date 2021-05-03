package pl.com.pslupski.letsDrive.order.application;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pl.com.pslupski.letsDrive.catalog.car.application.port.CarUseCase;
import pl.com.pslupski.letsDrive.catalog.car.db.CarJpaRepository;
import pl.com.pslupski.letsDrive.catalog.car.domain.Car;
import pl.com.pslupski.letsDrive.catalog.carItem.application.port.CarItemUseCase;
import pl.com.pslupski.letsDrive.catalog.carItem.application.port.CarItemUseCase.CreateCarItemCommand;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.CarItem;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.Category;
import pl.com.pslupski.letsDrive.catalog.carItem.domain.SubCategory;
import pl.com.pslupski.letsDrive.jpa.BaseEntity;
import pl.com.pslupski.letsDrive.order.application.port.CatalogInitializerUseCase;
import pl.com.pslupski.letsDrive.order.application.port.ModifyOrderUseCase;
import pl.com.pslupski.letsDrive.order.domain.Recipient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CatalogInitializerService implements CatalogInitializerUseCase {
    private final CarItemUseCase carItemCatalog;
    private final ModifyOrderUseCase placeOrder;
    private final CarJpaRepository carRepository;
    private final RestTemplate restTemplate;

    @Override
    @Transactional
    public void initialize() {
//        initData();
        initDataFromCsv();
        placeOrder();
    }

//        private void initData() {
//        Car car = carCatalog.addCar(new CarUseCase.CreateCarCommand("BMW", "X5", 2019, 4.4, "petrol"));
//        Car car2 = carCatalog.addCar(new CarUseCase.CreateCarCommand("Toyota",  "Prius", 2017, 1.5, "hybrid"));
//        Car car3 = carCatalog.addCar(new CarUseCase.CreateCarCommand("Volvo", "S60", 2014, 2.0, "petrol"));
//        Car car4 = carCatalog.addCar(new CarUseCase.CreateCarCommand("Fiat", "Punto", 2012, 1.2, "petrol"));
//        Car car5 = carCatalog.addCar(new CarUseCase.CreateCarCommand("Opel", "Mokka", 2017, 1.8, "diesel"));
//
//        carItemCatalog.addCarItem(new CarItemUseCase.CreateCarItemCommand("KJ5646T", new BigDecimal("123.67"), Category.CAR_PARTS, SubCategory.BRAKES, Set.of(car.getId()), 100L));
//        carItemCatalog.addCarItem(new CarItemUseCase.CreateCarItemCommand("KY5622I", new BigDecimal("255.45"), Category.CAR_PARTS, SubCategory.BRAKES, Set.of(car.getId(), car2.getId()), 100L));
//        carItemCatalog.addCarItem(new CarItemUseCase.CreateCarItemCommand("YU5231L", new BigDecimal("1200.00"), Category.CAR_PARTS, SubCategory.COOLING_AND_HEATING, Set.of(car3.getId(), car4.getId(), car5.getId()), 100L));
//    }

    private void initDataFromCsv() {
        ClassPathResource resource = new ClassPathResource("carParts.csv");
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            CsvToBean<CsvCarPart> build = new CsvToBeanBuilder<CsvCarPart>(reader)
                    .withType(CsvCarPart.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            build.stream().forEach(this::initCarPart);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse CSV file", e);
        }
    }

    private void initCarPart(CsvCarPart csvCarPart) {
        Set<Long> cars = Arrays.stream(csvCarPart.cars.split(","))
                .filter(StringUtils::isNoneBlank)
                .map(String::trim)
                .map(this::getOrCreateCar)
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());
        CreateCarItemCommand command = new CreateCarItemCommand(csvCarPart.getProductCode(), csvCarPart.getPrice(),
                csvCarPart.getCategory(), csvCarPart.getSubCategory(), cars, 25L);
        CarItem carItem = carItemCatalog.addCarItem(command);
        carItemCatalog.updateCarItemImage(updateCarItemImageCommand(carItem.getId(), csvCarPart.thumbnail));
        //TODO upload image/thumbnail of carPart
    }

    private CarItemUseCase.UpdateCarItemImageCommand updateCarItemImageCommand(Long carItemId, String thumbnailUrl) {
        ResponseEntity<byte[]> response = restTemplate.exchange(thumbnailUrl, HttpMethod.GET, null, byte[].class);
        String contentType = Objects.requireNonNull(response.getHeaders().getContentType()).toString();
        return new CarItemUseCase.UpdateCarItemImageCommand(carItemId, "thumbnail", response.getBody(), contentType);
    }

    private Car getOrCreateCar(String car) {
        String[] carString = car.split(" ");
        String manufacturer = carString[0];
        String model = carString[1];
        Integer year = Integer.parseInt(carString[2]);
        Double engine = Double.parseDouble(carString[3]);
        String fuel = carString[4];
        return carRepository.findByManufacturerAndModelAndYearAndEngineAndFuel(manufacturer, model, year, engine, fuel)
                .orElseGet(() -> carRepository.save(new Car(manufacturer, model, year, engine, fuel)));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CsvCarPart {
        @CsvBindByName
        private String productCode;
        @CsvBindByName
        private BigDecimal price;
        @CsvBindByName
        private Category category;
        @CsvBindByName
        private SubCategory subCategory;
        @CsvBindByName
        private String cars;
        @CsvBindByName
        private String thumbnail;
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

        ModifyOrderUseCase.PlaceOrderCommand command = ModifyOrderUseCase.PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new ModifyOrderUseCase.OrderItemCommand(carItem.getId(), 2))
                .item(new ModifyOrderUseCase.OrderItemCommand(carItem2.getId(), 3))
                .build();

        placeOrder.placeOrder(command);
    }
}