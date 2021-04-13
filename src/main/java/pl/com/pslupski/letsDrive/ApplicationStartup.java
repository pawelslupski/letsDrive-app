package pl.com.pslupski.letsDrive;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.com.pslupski.letsDrive.catalog.car.application.port.CarUseCase;

import static pl.com.pslupski.letsDrive.catalog.car.application.port.CarUseCase.CreateCarCommand;

@Component
public class ApplicationStartup implements CommandLineRunner {
    private final CarUseCase catalog;

    public ApplicationStartup(CarUseCase catalog) {
        this.catalog = catalog;
    }

    @Override
    public void run(String... args) throws Exception {
        initData();
    }

    private void initData() {
        catalog.addCar(new CreateCarCommand("BMW", "X5", 2019, 4.4, "petrol"));
        catalog.addCar(new CreateCarCommand("Toyota",  "Prius", 2017, 1.5, "hybrid"));
        catalog.addCar(new CreateCarCommand("Volvo", "S60", 2014, 2.0, "petrol"));
        catalog.addCar(new CreateCarCommand("Fiat", "Punto", 2012, 1.2, "petrol"));
        catalog.addCar(new CreateCarCommand("Opel", "Mokka", 2017, 1.8, "diesel"));
    }
}
