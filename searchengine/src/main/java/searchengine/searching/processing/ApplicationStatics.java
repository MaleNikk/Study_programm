package searchengine.searching.processing;

import lombok.Data;

@Data
public class ApplicationStatics {

    public static final String
            IN_PROGRESS = "INDEXING",
            INDEXED_STOP = "FAILED",
            INDEXING_COMPLETE = "INDEXED",
            REGEX_HTTP = "http",
            REGEX_POINT = "\"",
            REGEX_CHECK_LINK = "://",
            REGEX_HTML = "<",
            ERROR_SEARCH = "Задан пустой поисковый запрос",
            ERROR_CONNECTION = "Указанная страница не найдена",
            RESPONSE_OK = "Add site successful!";

    public static final Boolean
            TRUE = true,
            FALSE = false;

    public static final Integer
            COUNT_THREADS = 7,
            APP_ZERO = 0,
            TIME_SLEEP = 5000,
            COUNT_WORDS = 250,
            COUNT_SITES = 10,
            START_SAVE_SITE = 5,
            MIN_WORD_SIZE = 5,
            MIN_COUNT_WORDS = 5,
            INDEX_PRESENT = 2;

}
