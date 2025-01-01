package searchengine.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "allSites")
public class AllSitesEntity {

    @Id
    private Integer id;

    private ModelSite modelSite;
}
