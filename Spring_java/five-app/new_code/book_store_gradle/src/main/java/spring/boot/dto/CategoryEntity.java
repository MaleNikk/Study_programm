package spring.boot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class CategoryEntity {

    private Integer id;

    @NotBlank(message = "The text category must be credited")
    private String text;

    @NotBlank(message = "The description must be credited")
    private String description;
}
