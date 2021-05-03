package pl.com.pslupski.letsDrive.catalog.admin.web;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.pslupski.letsDrive.order.application.CatalogInitializerService;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private final CatalogInitializerService initializer;

    @PostMapping("/initialization")
    @Transactional
    public void initialize() {
        initializer.initialize();
    }
}
