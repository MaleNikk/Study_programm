package searchengine.config;

import searchengine.dto.entity.*;
import searchengine.dto.model.ModelQueryAnswer;
import searchengine.dto.model.TotalSearchResult;

import java.time.LocalDateTime;

public final class FixedValue {

    public static final String
            IN_PROGRESS = "INDEXING",
            INDEXED_STOP = "INDEXING STOP",
            INDEXING_COMPLETE = "INDEXED",
            FAILED = "FAILED",
            INDEXING_NOT_STARTED = "INDEXING NOT STARTED",
            REGEX_NO_ABC = "(?iu)\\s*(\\s|[^a-zA-Zа-яА-ЯёЁ])\\s*",
            REGEX_URL = "(?iu)\\|\\?|[а-яА-ЯёЁ]|\\=|\\;|\\}|\\!|\\&|\\@|\\<|\\>|\\{|.js|.png|.csv|.css",
            CHECK_LINK_HTTP = "http",
            ERROR_SEARCH = "Задан пустой поисковый запрос",
            ERROR_CONNECTION = "Некорректный формат добавляемой страницы или адреса не существует",
            RESPONSE_OK = "Запрос выполнен успешно!",
            NO_ERROR = "No errors were found!",
            FAILED_ERROR = "No connection to the Internet resource! Try again later! Or check the URL for errors!";


    public static final Boolean
            TRUE = true,
            FALSE = false;

    public static final Integer
            ZERO = 0,
            TIME_SLEEP = 5000;

    public static ModelParentSite getNewModelParentSite(String url, String name) {
        return new ModelParentSite(url, name, LocalDateTime.now().toString(),
                INDEXING_NOT_STARTED, System.nanoTime(), NO_ERROR, ZERO, ZERO);
    }

    public static TotalSearchResult getBadResponse() {
        return new TotalSearchResult(FALSE, ERROR_SEARCH);
    }

    public static ModelQueryAnswer getOkResponse() {
        return new ModelQueryAnswer(TRUE, RESPONSE_OK);
    }

    public static ModelQueryAnswer getBadResponseAddSite() {
        return new ModelQueryAnswer(FALSE, ERROR_CONNECTION);
    }
}
