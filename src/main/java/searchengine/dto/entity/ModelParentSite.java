package searchengine.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
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
