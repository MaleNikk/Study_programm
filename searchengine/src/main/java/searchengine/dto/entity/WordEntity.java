package searchengine.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;

@Data
@AllArgsConstructor
@Builder
@Document(collection = "words")
public class WordEntity {

    @Id
    private String word;

    private HashSet<String> parentsUrl;

    private HashSet<RegisteredSite> sites;
}
