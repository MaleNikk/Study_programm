package spring.boot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class BookEntity {

    private Integer id;

    private Integer category_id;

    @Positive(message = "Price will be present more null.")
    private Integer price;

    @NotBlank(message = "The author must be credited.")
    private String author;

    @NotBlank(message = "The title must be credited.")
    private String title;

    private String date_print;

}
