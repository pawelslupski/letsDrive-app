package pl.com.pslupski.letsDrive.uploads.domain;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class Upload {
    String id;
    String fileName;
    byte[] file;
    String contentType;
    LocalDateTime createdAt;
}
