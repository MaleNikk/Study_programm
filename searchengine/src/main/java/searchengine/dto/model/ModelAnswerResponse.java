package searchengine.dto.model;

import lombok.Data;
import searchengine.searching.processing.ApplicationStatics;

import java.util.HashSet;

@Data
public class ModelAnswerResponse {

    private Boolean result;

    private String error;

    private Integer count;

    private HashSet<ModelSiteResponse> data;

    public ModelAnswerResponse(Boolean result, Integer count, HashSet<ModelSiteResponse> data) {
        this.result = result;
        this.data = data;
        this.count = count;
    }

    public ModelAnswerResponse(Boolean result, String error) {
        this.result = result;
        this.error = error;
    }

    public static ModelAnswerResponse getBadResponse(){
        return new ModelAnswerResponse(ApplicationStatics.FALSE,ApplicationStatics.ERROR_SEARCH);
    }

    public static ModelAnswerResponse getOkResponse(){
        return new ModelAnswerResponse(ApplicationStatics.TRUE,ApplicationStatics.RESPONSE_OK);
    }

    public static ModelAnswerResponse getBadResponseAddSite() {
        return new ModelAnswerResponse(ApplicationStatics.FALSE, ApplicationStatics.ERROR_CONNECTION);
    }
}
