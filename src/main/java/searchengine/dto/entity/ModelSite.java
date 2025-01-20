package searchengine.dto.entity;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public record ModelSite(String url, String parentUrl, String name){}
