package searchengine.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "foundSites")
public class FoundSiteEntity {
    @Id
    private Integer id;

    private ModelSite modelSite;
}
