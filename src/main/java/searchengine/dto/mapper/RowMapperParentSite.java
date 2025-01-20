package searchengine.dto.mapper;

import org.springframework.jdbc.core.RowMapper;
import searchengine.dto.entity.ModelParentSite;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapperParentSite implements RowMapper<ModelParentSite> {
    @Override
    public ModelParentSite mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ModelParentSite(
                rs.getString("url"),
                rs.getString("name"),
                rs.getString("created_time"),
                rs.getString("status"),
                rs.getLong("status_time"),
                rs.getString("error"),
                rs.getInt("pages"),
                rs.getInt("lemmas"));
    }
}
