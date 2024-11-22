package com.example.web.news.storage.mapper;

import com.example.web.news.entity.MassageEntity;
import com.example.web.news.injections.mapper.MapperMassage;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@Component
public class RowMapperMassage implements MapperMassage<MassageEntity> {

    @Override
    public MassageEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MassageEntity(
                rs.getLong(MassageEntity.Fields.id),
                rs.getString(MassageEntity.Fields.author),
                rs.getString(MassageEntity.Fields.title),
                rs.getString(MassageEntity.Fields.date_resent));
    }

    @Override
    public RowMapperMassage createData(){
        return new RowMapperMassage();
    }
}
