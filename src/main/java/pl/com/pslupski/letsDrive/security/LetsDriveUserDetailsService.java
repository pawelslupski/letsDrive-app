package pl.com.pslupski.letsDrive.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.com.pslupski.letsDrive.user.domain.UserEntityDetails;
import pl.com.pslupski.letsDrive.user.domain.UserEntityRepository;

@AllArgsConstructor
public class LetsDriveUserDetailsService implements UserDetailsService {

    private final UserEntityRepository repository;
    private final AdminConfig config;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (config.getUsername().equalsIgnoreCase(username)) {
            return config.adminUser();
        }
        return repository.findByUsernameIgnoreCase(username)
                .map(UserEntityDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}