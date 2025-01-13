package searchengine.dto.model;

import searchengine.searching.processing.FixedValue;

public record ModelStartStop(Boolean result, String error) {

    public static ModelStartStop startIndexing() {
        return new ModelStartStop(FixedValue.TRUE, "Indexing started!");
    }

    public static ModelStartStop wasStarted() {
        return new ModelStartStop(FixedValue.TRUE, "Indexing in progress!");
    }

    public static ModelStartStop stopIndexing() {
        return new ModelStartStop(FixedValue.TRUE, "Indexing stopped!");
    }

    public static ModelStartStop notStarted() {
        return new ModelStartStop(FixedValue.FALSE, "Indexing not started!");
    }
}

