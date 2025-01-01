package searchengine.searching.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import searchengine.dto.entity.AllSitesEntity;

@Repository
@Primary
public interface RepositoryAllSite extends MongoRepository<AllSitesEntity,Integer> {
}
