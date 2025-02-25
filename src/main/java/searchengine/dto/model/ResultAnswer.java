package searchengine.dto.model;

public record ResultAnswer(
        String site,
        String siteName,
        String uri,
        String title,
        String snippet,
        Double relevance) {}
