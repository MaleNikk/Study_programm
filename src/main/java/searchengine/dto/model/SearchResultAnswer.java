package searchengine.dto.model;

public record SearchResultAnswer(
        String site,
        String siteName,
        String uri,
        String title,
        String snippet,
        Double relevance) {}
