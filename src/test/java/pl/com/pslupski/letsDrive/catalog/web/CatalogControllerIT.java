package pl.com.pslupski.letsDrive.catalog.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.com.pslupski.letsDrive.catalog.car.application.port.CarUseCase;
import pl.com.pslupski.letsDrive.catalog.car.domain.Car;
import pl.com.pslupski.letsDrive.catalog.car.web.CarController;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CatalogControllerIT {

    @Autowired
    CarController carController;
    @Autowired
    CarUseCase carUseCase;

    @BeforeEach
    void setUp() {
        carUseCase.addCar(new CarUseCase.CreateCarCommand("Mercedes", "Benz", 2002, 2.0, "petrol"));
        carUseCase.addCar(new CarUseCase.CreateCarCommand("Fiat", "Punto", 2001, 1.4, "petrol"));
        carUseCase.addCar(new CarUseCase.CreateCarCommand("Porshe", "Cayenne", 2012, 2.5, "petrol"));
        carUseCase.addCar(new CarUseCase.CreateCarCommand("Opel", "Mokka", 2005, 1.7, "diesel"));
        carUseCase.addCar(new CarUseCase.CreateCarCommand("Fiat", "Brava", 2007, 1.6, "petrol"));
    }

    @Test
    public void getAllCars() {
        // Given

        // When
        List<Car> allCars = carController.getAll(5);
        //Then
        Assertions.assertEquals(5, allCars.size());
    }
}