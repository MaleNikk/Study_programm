package searchengine.searching.processing;

import lombok.Data;
import searchengine.dto.entity.*;
import searchengine.dto.model.TotalSearchResult;

import java.time.LocalDateTime;

@Data
public final class FixedValue {

    public static final String
            SEARCH_IN_ALL = "All",
            IN_PROGRESS = "INDEXING",
            INDEXED_STOP = "STOP MANUALLY",
            INDEXING_COMPLETE = "INDEXED",
            INDEXING_NOT_STARTED = "INDEXING NOT STARTED",
            REGEX_ABC = "[^a-zA-Zа-яА-ЯёЁ]",
            REGEX_NO_ABC = "[a-zA-Zа-яА-ЯёЁ]",
            CHECK_LINK_HTTP = "http://",
            CHECK_LINK_HTTPS = "https://",
            CHECK_LINK_HTML = "href",
            ERROR_SEARCH = "Задан пустой поисковый запрос",
            ERROR_CONNECTION = "Указанная страница не найдена",
            RESPONSE_OK = "Запрос выполнен успешно!",
            ERROR = "Something wrong, please read log file!";

    public static final Boolean
            TRUE = true,
            FALSE = false;

    public static final Integer
            COUNT_THREADS = 9,
            ZERO = 0,
            TIME_SLEEP = 5000,
            COUNT_WORDS = 500,
            COUNT_SITES = 50;

    public static ModelParentSite getNewModelParentSite(String url, String name) {
        return new ModelParentSite(url, name, LocalDateTime.now().toString(), "", System.nanoTime(),
                FixedValue.ERROR, 0, 0);
    }

    public static ModelSite getNewModelSite(String url, String parentUrl, String name){
        return new ModelSite(url, parentUrl, name);
    }

    public static TotalSearchResult getBadResponse(){
        return new TotalSearchResult(FixedValue.FALSE, FixedValue.ERROR_SEARCH);
    }

    public static TotalSearchResult getOkResponse(){
        return new TotalSearchResult(FixedValue.TRUE, FixedValue.RESPONSE_OK);
    }

    public static TotalSearchResult getBadResponseAddSite() {
        return new TotalSearchResult(FixedValue.FALSE, FixedValue.ERROR_CONNECTION);
    }
}
