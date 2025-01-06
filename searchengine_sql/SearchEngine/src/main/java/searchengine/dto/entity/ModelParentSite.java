package searchengine.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@AllArgsConstructor
@Data
@FieldNameConstants
public class ModelParentSite {
    private String url;
    private String name;
    private String createdTime;
    private String status;
    private long statusTime;
    private String error;
    private int pages;
    private int lemmas;
}
