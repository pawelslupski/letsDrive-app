package pl.com.pslupski.letsDrive;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.com.pslupski.letsDrive.catalog.application.port.CarUseCase;

import java.math.BigDecimal;

import static pl.com.pslupski.letsDrive.catalog.application.port.CarUseCase.*;

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
        catalog.addCar(new CreateCarCommand("BMW X5", 2015, new BigDecimal(45), 75000));
        catalog.addCar(new CreateCarCommand("Toyota Prius", 2017, new BigDecimal(32), 107560));
        catalog.addCar(new CreateCarCommand("Volvo S60", 2014, new BigDecimal(39), 125450));
        catalog.addCar(new CreateCarCommand("Fiat Punto", 2012, new BigDecimal(20), 167560));
        catalog.addCar(new CreateCarCommand("Opel Mokka", 2014, new BigDecimal(37), 45452));
    }
}
