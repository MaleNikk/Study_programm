package com.example.web.news.storage.management;

import com.example.web.news.entity.ReaderEntity;
import com.example.web.news.exception.SearchException;
import com.example.web.news.injections.management.ManagementReaders;
import com.example.web.news.injections.mapper.MapperReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

@Getter
@Repository
@Qualifier
@RequiredArgsConstructor
@Slf4j
public class StorageReaders implements ManagementReaders<ReaderEntity> {

    private final JdbcTemplate template;
    private final HashSet<Long> idReaders;
    private final MapperReader<ReaderEntity> mapperReader;

    @Override
    public List<ReaderEntity> searchAll() {
        log.info("Enjoy void searchAll of StorageReaders");
        String sql = "SELECT * FROM readers";
        return getTemplate().query(sql,getMapperReader().createData());
    }

    @Override
    public ReaderEntity searchById(Long id) {
        log.info("Enjoy void searchById of StorageReaders");
        String sql = "SELECT * FROM readers WHERE id = ? ";
        return DataAccessUtils.singleResult(
                getTemplate().query(sql,
                        new ArgumentPreparedStatementSetter(new Object[] {id}),
                        new RowMapperResultSetExtractor<>(getMapperReader().createData(),1)));
    }

    @Override
    public ReaderEntity save(ReaderEntity readerEntity) {
        getIdReaders().add(readerEntity.getId());
        String sql = "INSERT INTO readers (id, pin, name, surname) VALUES(?, ?, ?, ?)";
        log.info("Enjoy void save of StorageReaders");
        getTemplate().update(sql,
                readerEntity.getId(),
                readerEntity.getPin(),
                readerEntity.getName(),
                readerEntity.getSurname());
        return readerEntity;
    }

    @Override
    public ReaderEntity edit(ReaderEntity readerEntity) {
        log.info("Enjoy void edit of StorageReaders");
            String sql = "UPDATE readers SET name = ? WHERE id = ?";
            getTemplate().update(sql,
                    readerEntity.getName(),
                    readerEntity.getId());
            return readerEntity;
    }

    @Override
    public void removeById(Long id) {
        log.info("Enjoy void removeById of StorageReaders");
        String sql = "DELETE FROM readers WHERE id = ?";
        getTemplate().update(sql,id);
    }

    @Override
    public void batchInsert(List<ReaderEntity> readerEntities) {
        log.info("Enjoy void batchInsert of StorageReaders");
        String sql = "INSERT INTO readers (id, pin, name, surname) VALUES (?, ?, ?, ?)";
        getTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ReaderEntity readerEntity = readerEntities.get(i);
                ps.setLong(1,readerEntity.getId());
                ps.setLong(2,readerEntity.getPin());
                ps.setString(3,readerEntity.getName());
                ps.setString(4,readerEntity.getSurname());
                getIdReaders().add(readerEntity.getId());
            }

            @Override
            public int getBatchSize() {
                return readerEntities.size();
            }
        });

    }

    @Override
    public HashSet<Long> numbersId() {
        log.info("Enjoy void numbersId of StorageReaders");
        searchAll().forEach(reader -> getIdReaders().add(reader.getId()));
        return idReaders;
    }
}
