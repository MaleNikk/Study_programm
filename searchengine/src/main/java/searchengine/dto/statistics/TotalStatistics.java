package searchengine.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TotalStatistics {
    private int sites;
    private int pages;
    private int lemmas;
    private boolean indexing;
}
