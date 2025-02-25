package searchengine.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalSearchResult {

    private Boolean result;

    private Integer count;

    private String error;

    private List<ResultAnswer> data;

    public TotalSearchResult(Boolean result, String error) {
        this.result = result;
        this.error = error;
    }
}
