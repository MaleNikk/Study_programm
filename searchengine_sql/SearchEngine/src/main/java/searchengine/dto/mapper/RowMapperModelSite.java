package searchengine.dto.mapper;

import org.springframework.jdbc.core.RowMapper;
import searchengine.dto.entity.ModelSite;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapperModelSite implements RowMapper<ModelSite> {
    @Override
    public ModelSite mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ModelSite(
                rs.getString(ModelSite.Fields.url),
                rs.getString(ModelSite.Fields.parentUrl),
                rs.getString(ModelSite.Fields.name)
        );
    }
}
