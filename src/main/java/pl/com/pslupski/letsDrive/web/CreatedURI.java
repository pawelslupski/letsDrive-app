package pl.com.pslupski.letsDrive.web;

import lombok.AllArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@AllArgsConstructor
public class CreatedURI {
    private final String path;

    public URI uri() {
        return ServletUriComponentsBuilder.fromCurrentRequestUri().path(path).build().toUri();
    }
}