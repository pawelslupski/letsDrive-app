package pl.com.pslupski.letsDrive.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AllArgsConstructor
public class JsonUsernameAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoggingCommand command = mapper.readValue(request.getReader(), LoggingCommand.class);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                command.getUsername(), command.getPassword()
        );
        return this.getAuthenticationManager().authenticate(token);
    }
}