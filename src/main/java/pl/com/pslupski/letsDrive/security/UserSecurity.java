package pl.com.pslupski.letsDrive.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserSecurity {

    public boolean isOwnerOrAdmin(UserDetails user, String objectOwner) {
        return isAdmin(user) || isOwner(user, objectOwner);
    }

    private boolean isAdmin(UserDetails user) {
        return user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_ADMIN"));
    }

    private boolean isOwner(UserDetails user, String objectOwner) {
        return user.getUsername().equalsIgnoreCase(objectOwner);
    }
}