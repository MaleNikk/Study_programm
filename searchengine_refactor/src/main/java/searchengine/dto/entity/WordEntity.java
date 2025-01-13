package searchengine.dto.entity;

import lombok.AllArgsConstructor;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "bigWords")
public class WordEntity {

    @Id
    private String id;

    private ModelWord modelWord;
}
