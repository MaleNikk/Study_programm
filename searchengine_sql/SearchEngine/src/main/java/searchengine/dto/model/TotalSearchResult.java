package searchengine.dto.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TotalSearchResult {

    private Boolean result;

    private Integer count;

    private String error;

    private List<SearchResultAnswer> data;

    public TotalSearchResult(Boolean result, String error) {
        this.result = result;
        this.error = error;
    }
}
