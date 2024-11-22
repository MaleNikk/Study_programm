package com.example.web.news.storage.management;

import com.example.web.news.entity.CommentEntity;
import com.example.web.news.entity.ReaderEntity;
import com.example.web.news.exception.SearchException;
import com.example.web.news.injections.mapper.MapperComment;
import com.example.web.news.injections.management.ManagementComments;
import com.example.web.news.injections.service.ServiceReaders;
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
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Qualifier
@RequiredArgsConstructor
@Slf4j
public class StorageComments implements ManagementComments<CommentEntity> {

    private final JdbcTemplate jdbcTemplate;
    @Getter
    private final HashSet<Long> numbersCommentsId;

    private final MapperComment<CommentEntity> mapperComment;

    private final ServiceReaders<ReaderEntity> serviceReaders;

    @Override
    public List<CommentEntity> searchAll() {
        log.info("Enjoy StorageComments method search all.");
        String sql = "SELECT * FROM comments";
        return jdbcTemplate.query(sql, mapperComment.createData());
    }

    @Override
    public CommentEntity searchById(Long id) {
        log.info("Enjoy StorageComments method search by id: {}.", id);
        String sql = "SELECT * FROM comments WHERE id = ?";
        return DataAccessUtils.singleResult(
                jdbcTemplate.query(sql,
                        new ArgumentPreparedStatementSetter(new Object[]{id}),
                        new RowMapperResultSetExtractor<>(mapperComment.createData(), 1)));
    }

    @Override
    public CommentEntity save(CommentEntity commentEntity) {
        if (isPresentPinReader(commentEntity.getPin_reader())) {
            getNumbersCommentsId().add(commentEntity.getId());
            log.info("Enjoy StorageComment method save commentEntity: {}.", commentEntity);
            String sql = "INSERT INTO comments (id, id_news,pin_reader, author, text, date_comment) VALUES(?, ? ,?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    commentEntity.getId(),
                    commentEntity.getId_news(),
                    commentEntity.getPin_reader(),
                    commentEntity.getAuthor(),
                    commentEntity.getText(),
                    commentEntity.getDate_comment());
            return commentEntity;
        } else {
            return null;
        }
    }

    @Override
    public CommentEntity edit(CommentEntity comment) {
        if (isPresentPinReader(comment.getPin_reader())) {
            log.info("Enjoy StorageComments method edit commentEntity: {}.", comment);
            CommentEntity commentEdited = searchById(comment.getId());
            if (commentEdited != null) {
                String sql = "UPDATE comments SET text = ? WHERE id = ?";
                jdbcTemplate.update(sql,
                        comment.getText(),
                        comment.getId());
                return comment;
            }
        }

        log.info("CommentEntity {} for edited not fount", comment);
        throw new SearchException(MessageFormat.format("Comment: {0} are not present in data base!", comment));

    }

    @Override
    public void removeById(Long id) {
        log.info("Enjoy StorageComments method remove by id: {}.", id);
        String sql = "DELETE FROM comments WHERE id = ?";
        jdbcTemplate.update(sql, id);
        getNumbersCommentsId().remove(id);
    }

    @Override
    public void batchInsert(List<CommentEntity> commentEntities) {
        log.info("Enjoy StorageComments method batchInsert. Load default list comments.");
        String sql = "INSERT INTO comments (id, id_news, pin_reader, author, text, date_comment) VALUES (?, ? ,?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CommentEntity commentEntity = commentEntities.get(i);
                ps.setLong(1, commentEntity.getId());
                ps.setLong(2, commentEntity.getId_news());
                ps.setLong(3, commentEntity.getPin_reader());
                ps.setString(4, commentEntity.getAuthor());
                ps.setString(5, commentEntity.getText());
                ps.setString(6, commentEntity.getDate_comment());
                numbersCommentsId.add(commentEntity.getId());
            }

            @Override
            public int getBatchSize() {
                return commentEntities.size();
            }
        });

    }

    @Override
    public HashSet<Long> numbersId() {
        log.info("Enjoy StorageComments method numbersCommentsId.");
        for (CommentEntity comment : searchAll()) {
            getNumbersCommentsId().add(comment.getId());
        }
        return numbersCommentsId;
    }

    @Override
    public List<CommentEntity> commentsByNewsId(Long id) {
        log.info("Enjoy StorageComments method commentsByNewsId.");
        return searchAll().stream().filter(comment -> id == comment.getId_news()).toList();
    }

    @Override
    public List<CommentEntity> commentsByAuthorName(String author) {
        log.info("Enjoy StorageComments method commentsByAuthorName.");
        return searchAll().stream().filter(comment -> Objects.equals(author, comment.getAuthor())).toList();
    }

    @Override
    public boolean isPresentPinReader(Long pin) {
        log.info("Enjoy StorageComments method isPresentPinReader.");
        boolean  checkPin = false;
        for (ReaderEntity reader : serviceReaders.searchAll()) {
            if (reader.getPin().equals(pin)) {
                checkPin = true;
                break;
            }
        }
        return checkPin;
    }
}
