package pl.com.pslupski.letsDrive.users.application.port;

import pl.com.pslupski.letsDrive.commons.Either;
import pl.com.pslupski.letsDrive.user.domain.UserEntity;

public interface UsersRegistrationUseCase {

    RegisterResponse register(String username, String password);

    class RegisterResponse extends Either<String, UserEntity> {
        public RegisterResponse(boolean success, String left, UserEntity right) {
            super(success, left, right);
        }

        public static RegisterResponse success(UserEntity userEntity) {
            return new RegisterResponse(true, null, userEntity);
        }
        public static RegisterResponse failure(String error) {
            return new RegisterResponse(false, error, null);
        }
    }
}