package searchengine.dto.mapper;

import org.springframework.jdbc.core.RowMapper;
import searchengine.dto.entity.ModelSite;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapperModelSite implements RowMapper<ModelSite> {
    @Override
    public ModelSite mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ModelSite(
                rs.getString("url"),
                rs.getString("parent_url"),
                rs.getString("name")
        );
    }
}
