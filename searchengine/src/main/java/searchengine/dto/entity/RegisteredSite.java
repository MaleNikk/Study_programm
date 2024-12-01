package searchengine.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import searchengine.dto.statistics.DetailedStatisticsItem;


@AllArgsConstructor
@Data
@Document(collection = "registered_sites")
public class RegisteredSite {
    @Id
    private String url;

    private String parentUrl;

    private String name;

    private Integer countWords;

    private Integer pages;
}
