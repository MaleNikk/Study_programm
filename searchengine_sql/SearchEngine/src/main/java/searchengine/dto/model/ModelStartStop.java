package searchengine.dto.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import searchengine.searching.processing.FixedValue;

@NoArgsConstructor
@Data
public class ModelStartStop {

    private Boolean result;

    private String error;

    public ModelStartStop(Boolean result, String error) {
        this.result = result;
        this.error = error;
    }

    public static ModelStartStop startIndexing() { return new ModelStartStop(FixedValue.TRUE,"Indexing started!");
    }

    public static ModelStartStop wasStarted() {
        return new ModelStartStop(FixedValue.TRUE, "Indexing in progress!");
    }

    public static ModelStartStop stopIndexing(){
       return  new ModelStartStop(FixedValue.TRUE, "Indexing stopped!");
    }

    public static ModelStartStop notStarted(){
        return new ModelStartStop(FixedValue.FALSE, "Indexing not started!");
    }
}

