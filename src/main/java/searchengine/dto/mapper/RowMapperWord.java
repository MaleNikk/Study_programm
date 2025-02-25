package searchengine.dto.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import searchengine.dto.entity.ModelWord;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RowMapperWord implements RowMapper<ModelWord> {
    @Override
    public ModelWord mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ModelWord(
                rs.getString("lemma"),
                rs.getString("url"),
                rs.getString("parent_url"),
                rs.getString("name"),
                rs.getInt("frequency")
        );
    }
}
