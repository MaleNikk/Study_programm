package searchengine.dto.mapper;

import org.springframework.jdbc.core.RowMapper;
import searchengine.dto.entity.ModelWord;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapperWord implements RowMapper<ModelWord> {
    @Override
    public ModelWord mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ModelWord(
                rs.getString("lemma"),
                rs.getString("word"),
                rs.getString("url"),
                rs.getString("name"),
                rs.getString("parent_url")
        );
    }
}
