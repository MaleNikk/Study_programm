package com.example.web.news.storage.management;

import com.example.web.news.entity.MassageEntity;
import com.example.web.news.injections.management.ManagementMassage;
import com.example.web.news.injections.mapper.MapperMassage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
@Qualifier
@RequiredArgsConstructor
@Slf4j
public class StorageMassage implements ManagementMassage {

    private final JdbcTemplate template;

    private final MapperMassage<MassageEntity> mapperMassage;

    @Override
    public MassageEntity save(MassageEntity massageEntity) {
        log.info("Called method save from StorageMassage class.");
        massageEntity.setId((long) (getAll().size() + 1));
        massageEntity.setDate_resent(Instant.now().toString());
        String sql = "INSERT INTO questions (id, author, title, date_resent) VALUES(?, ?, ?, ?)";
        template.update(sql,
                massageEntity.getId(),
                massageEntity.getAuthor(),
                massageEntity.getTitle(),
                massageEntity.getDate_resent());
        return massageEntity;
    }

    @Override
    public boolean delete(Long id) {
        log.info("Called method delete from StorageMassage class.");
        String sql = "DELETE FROM questions WHERE id = ?";
        template.update(sql, id);
        return getAll().stream().noneMatch(m -> m.getId().equals(id));
    }

    @Override
    public List<MassageEntity> getAll() {
        log.info("Called method getAll from StorageMassage class.");
        String sql = "SELECT * FROM questions";
        return template.query(sql, mapperMassage.createData());
    }
}
