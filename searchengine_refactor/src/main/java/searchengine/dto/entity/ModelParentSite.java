package searchengine.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
;

@AllArgsConstructor
@Data
public class ModelParentSite {
    private String url;
    private String name;
    private LocalDateTime createdTime;
    private String status;
    private long statusTime;
    private String error;
    private Integer pages;
    private Integer lemmas;
}
