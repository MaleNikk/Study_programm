package searchengine.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModelSearch {

    private String word;

    private String parentSite;

    private Integer offset;

    private Integer limit;
}
