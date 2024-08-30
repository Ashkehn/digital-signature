

package webeid.example.dto;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileDTO implements Serializable {
    private static final String EXAMPLE_FILENAME = "example-for-signing.txt";

    private final String name;
    private String contentType;
    private byte[] contentBytes;

    public FileDTO(String name) {
        this.name = name;
    }

    public FileDTO(String name, String contentType, byte[] contentBytes) {
        this.name = name;
        this.contentType = contentType;
        this.contentBytes = contentBytes;
    }

    public static FileDTO getExampleForSigningFromResources() throws IOException {
        final URI resourceUri = new ClassPathResource("/static/files/" + EXAMPLE_FILENAME).getURI();
        return new FileDTO(
                EXAMPLE_FILENAME,
                MimeTypeUtils.TEXT_PLAIN_VALUE,
                Files.readAllBytes(Paths.get(resourceUri))
        );
    }

    public String getName() {
        return name;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getContentBytes() {
        return contentBytes;
    }
}
