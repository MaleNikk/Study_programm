package searchengine.searching.injections.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import searchengine.dto.entity.SiteEntity;

@Repository
@Qualifier
public interface AppRepositorySite extends MongoRepository<SiteEntity,String> {
}
