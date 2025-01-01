package searchengine.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "sysSites")
public class SystemSiteEntity {

    @Id
    private Integer id;

    private String message;

    private ModelSite modelSite;
}
