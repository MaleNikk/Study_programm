package searchengine.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModelQueryAnswer  {
    private Boolean result;
    private String error;
}
