package pl.com.pslupski.letsDrive.security;

import lombok.Data;

@Data
public class LoggingCommand {
    private String username;
    private String password;
}