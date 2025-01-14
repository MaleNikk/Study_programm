package searchengine.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "parentSites")
public class ParentSiteEntity {

    @Id
    private Integer id;

    private ModelParentSite modelParentSite;
}