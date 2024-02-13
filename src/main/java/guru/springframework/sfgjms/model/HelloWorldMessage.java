package guru.springframework.sfgjms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelloWorldMessage implements Serializable {
    @Serial
    static final long serialVersionUID = -1484599584412354884L;

    private UUID id;
    private String message;
}
