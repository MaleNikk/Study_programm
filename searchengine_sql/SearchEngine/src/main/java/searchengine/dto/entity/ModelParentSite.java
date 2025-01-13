package searchengine.dto.entity;


public record ModelParentSite(String url, String name, String createdTime, String status,
                              long statusTime, String error, int pages, int lemmas) {
}
