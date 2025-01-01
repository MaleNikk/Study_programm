package searchengine.searching.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import searchengine.dto.entity.WordEntity;

@Repository
@Qualifier
public interface WordsRepository extends MongoRepository<WordEntity, String> {
}
