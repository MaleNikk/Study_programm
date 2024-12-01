package searchengine.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;



@Data
@AllArgsConstructor
@Document(collection = "basic_sites")
public class BasicSiteEntity {

    @Id
    private String url;

    private String name;

    private LocalDateTime createdTime;
}