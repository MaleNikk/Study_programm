package searchengine.dto.entity;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public record ModelWord(String lemma, String word, String url, String name, String parentUrl) {}
