package searchengine.dto.mapper;

import org.springframework.jdbc.core.RowMapper;
import searchengine.dto.entity.ModelParentSite;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapperParentSite implements RowMapper<ModelParentSite> {
    @Override
    public ModelParentSite mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ModelParentSite(
                rs.getString(ModelParentSite.Fields.url),
                rs.getString(ModelParentSite.Fields.name),
                rs.getString(ModelParentSite.Fields.createdTime),
                rs.getString(ModelParentSite.Fields.status),
                rs.getLong(ModelParentSite.Fields.statusTime),
                rs.getString(ModelParentSite.Fields.error),
                rs.getInt(ModelParentSite.Fields.lemmas),
                rs.getInt(ModelParentSite.Fields.pages));
    }
}
