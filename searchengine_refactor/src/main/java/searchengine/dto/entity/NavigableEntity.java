package searchengine.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;

@Data
@AllArgsConstructor
@Document(collection = "navigation")
public class NavigableEntity {

    @Id
    private String id;

    private HashSet<String> words;
}
