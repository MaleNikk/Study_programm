package searchengine.searching.injections.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import searchengine.dto.entity.BasicSiteEntity;

@Repository
@Qualifier
public interface AppBasicSiteRepository extends MongoRepository<BasicSiteEntity, String> {
}
