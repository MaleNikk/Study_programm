package searchengine.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import searchengine.dto.entity.RegisteredSite;
import searchengine.searching.processing.ApplicationConnection;

@Data
@AllArgsConstructor
public class ModelSiteResponse {

    private String site;

    private String siteName;

    private String uri;

    private String tittle;

    private String snippet;

    private Double relevance;

    public static ModelSiteResponse from(RegisteredSite registeredSite,String word, Double relevance){
        String uri = registeredSite.getUrl().replaceAll(registeredSite.getParentUrl(),"");
        return new ModelSiteResponse(
                registeredSite.getParentUrl(),
                registeredSite.getName(),
                uri,
                ApplicationConnection.getTitleFromSite(registeredSite.getUrl()),
                ApplicationConnection.getSnippet(registeredSite.getUrl(), word),
                relevance);
    }
}
