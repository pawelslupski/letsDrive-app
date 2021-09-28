package pl.com.pslupski.letsDrive.users.application;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.pslupski.letsDrive.user.domain.UserEntity;
import pl.com.pslupski.letsDrive.user.domain.UserEntityRepository;
import pl.com.pslupski.letsDrive.users.application.port.UsersRegistrationUseCase;

@AllArgsConstructor
@Service
public class UserService implements UsersRegistrationUseCase {
    private final UserEntityRepository repository;
    private PasswordEncoder encoder;

    @Override
    public RegisterResponse register(String username, String password) {
        if (repository.findByUsernameIgnoreCase(username).isPresent()) {
            return RegisterResponse.failure("Account already exists!");
        } else {
            return RegisterResponse.success(repository.save(new UserEntity(username, encoder.encode(password))));
        }
    }
}