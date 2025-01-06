package searchengine.dto.mapper;

import org.springframework.jdbc.core.RowMapper;
import searchengine.dto.entity.ModelWord;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapperWord implements RowMapper<ModelWord> {
    @Override
    public ModelWord mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ModelWord(
                rs.getString(ModelWord.Fields.lemma),
                rs.getString(ModelWord.Fields.word),
                rs.getString(ModelWord.Fields.url),
                rs.getString(ModelWord.Fields.name),
                rs.getString(ModelWord.Fields.parentUrl)
        );
    }
}
