package pl.com.pslupski.letsDrive.users.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.pslupski.letsDrive.users.application.port.UsersRegistrationUseCase;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersRegistrationUseCase register;

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody RegisterCommand command) {
        return register.register(command.getUsername(), command.getPassword())
                .handle(
                        entity -> ResponseEntity.accepted().build(),
                        error -> ResponseEntity.badRequest().body(error)
                );
    }

    @Data
    private static class RegisterCommand {
        @Email
        String username;
        @Size(min = 3, max = 100)
        String password;
    }
}