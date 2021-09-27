package pl.com.pslupski.letsDrive.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class UserSecurity {

    public boolean isOwnerOrAdmin(User user, String objectOwner) {
        return isAdmin(user) || isOwner(user, objectOwner);
    }

    private boolean isAdmin(User user) {
        return user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_ADMIN"));
    }

    private boolean isOwner(User user, String objectOwner) {
        return user.getUsername().equalsIgnoreCase(objectOwner);
    }
}