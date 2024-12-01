package searchengine.dto.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ModelStartStop {

    private Boolean result;

    private String error;

    public ModelStartStop(Boolean result) {
        this.result = result;
    }

    public ModelStartStop(Boolean result, String error) {
        this.result = result;
        this.error = error;
    }
}
