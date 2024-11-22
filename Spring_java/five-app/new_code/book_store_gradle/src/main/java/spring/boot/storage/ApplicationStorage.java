package spring.boot.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import spring.boot.aop.CheckLog;
import spring.boot.configuration.AppCacheProperties;
import spring.boot.dto.ApplicationEntity;
import spring.boot.injector.BookMapper;
import spring.boot.injector.CategoryMapper;
import spring.boot.injector.StorageManagement;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Repository
@EnableCaching
@CacheConfig(cacheManager = "redisManager")
public class ApplicationStorage implements StorageManagement<ApplicationEntity>, Serializable {

    @Autowired
    private JdbcTemplate template;

    private final HashSet<Integer>
            idCategory = new HashSet<>(),
            idBook = new HashSet<>();

    @Autowired
    private BookMapper<ApplicationEntity> bookMapper;

    @Autowired
    private CategoryMapper<ApplicationEntity> categoryMapper;

    @Override
    @CheckLog
    @Cacheable("ViewAllCache")
    public List<ApplicationEntity> viewAll(String nameTable) {
        log.debug("Initial method vew all on application storage at time: {}", System.currentTimeMillis());
        String sql = "SELECT * FROM ".concat(nameTable);
        List<ApplicationEntity> list;
        if (checkNameTable(nameTable)){
            list = template.query(sql, bookMapper.createData());
            list.forEach(b -> idBook.add(b.getIntegers()[0]));
        } else {
            list = template.query(sql, categoryMapper.createData());
            list.forEach(b -> idCategory.add(b.getIntegers()[0]));
        }
        return list;
    }

    @Override
    @CheckLog
    @Cacheable("ViewByAuthorCache")
    public List<ApplicationEntity> viewAllByAuthor(String nameTable, String author) {
        log.debug("Initial method vew all by author on application storage at time: {}", System.currentTimeMillis());
        String sql = "SELECT * FROM ".concat(nameTable).concat(" WHERE author = '".concat(author).concat("'"));
        return template.query(sql, bookMapper);
    }

    @Override
    @CheckLog
    @Cacheable("ViewByCategory")
    public List<ApplicationEntity> viewAllByCategory(String nameTable, Integer category_id) {
        String sql = "SELECT * FROM ".concat(nameTable).concat(" WHERE category_id = "
                .concat(String.valueOf(category_id)));
        return template.query(sql, bookMapper);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "ViewAllCache",allEntries = true),
            @CacheEvict(value = "ViewByAuthorCache",allEntries = true),
            @CacheEvict(value = "ViewByCategory",allEntries = true)
    })
    public ApplicationEntity getById(Integer id, String nameTable) {
        String sql = "SELECT * FROM ".concat(nameTable)
                .concat(" WHERE id = '").concat(String.valueOf(id)).concat("'");
        if (checkNameTable(nameTable)) {
            return template.query(sql, bookMapper).getFirst();
        } else {
            return template.query(sql, categoryMapper).getFirst();
        }
    }

    @Override
        @Caching(evict = {
            @CacheEvict(value = "ViewAllCache",allEntries = true),
            @CacheEvict(value = "ViewByAuthorCache",allEntries = true),
            @CacheEvict(value = "ViewByCategory",allEntries = true)
    })
    public ApplicationEntity update(Integer id, String nameTable, ApplicationEntity updated) {
        if (checkNameTable(nameTable)) {
            String sql = "UPDATE "
                    .concat(nameTable)
                    .concat(" SET price = ?, category_id = ?, title = ? WHERE id = ?");
            template.update(sql,
                    updated.getIntegers()[1],
                    updated.getIntegers()[2],
                    updated.getStrings()[1],
                    updated.getIntegers()[0]);
        } else {
            String sql = "UPDATE "
                    .concat(nameTable)
                    .concat(" SET text = ?, description = ? WHERE id = ?");
            template.update(sql,
                    updated.getStrings()[0],
                    updated.getStrings()[1],
                    updated.getIntegers()[0]);
        }
        return getById(id,nameTable);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "ViewAllCache",allEntries = true),
            @CacheEvict(value = "ViewByAuthorCache",allEntries = true),
            @CacheEvict(value = "ViewByCategory",allEntries = true)
    })
    public ApplicationEntity save(ApplicationEntity savedEntity, String nameTable) {
        if (checkNameTable(nameTable)) {
            savedEntity.getIntegers()[0] = generateId(nameTable);
            savedEntity.getStrings()[2] = String.valueOf(LocalDateTime.now());
            String sql = "INSERT INTO "
                    .concat(nameTable)
                    .concat(" (id, price, category_id, author, title, date_print) VALUES(?, ?, ?, ?, ?, ?)");
            return loadData(sql, nameTable, savedEntity);
        } else {
            savedEntity.getIntegers()[0] = generateId(nameTable);
            String sql = "INSERT INTO "
                    .concat(nameTable)
                    .concat(" (id, text, description) VALUES(?, ?, ?)");
            return loadData(sql, nameTable, savedEntity);
        }
    }
    @Override
    public void batchInsert(List<ApplicationEntity> applicationEntity, String nameTable) {
        if (checkNameTable(nameTable)) {
            String sql = "INSERT INTO "
                    .concat(nameTable)
                    .concat(" (id, price, category_id, author, title, date_print) VALUES (?, ?, ?, ?, ?, ?)");
            template.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ApplicationEntity entity = applicationEntity.get(i);
                    ps.setInt(1, entity.getIntegers()[0]);
                    ps.setInt(2, entity.getIntegers()[1]);
                    ps.setInt(3, entity.getIntegers()[2]);
                    ps.setString(4, entity.getStrings()[0]);
                    ps.setString(5, entity.getStrings()[1]);
                    ps.setString(6, entity.getStrings()[2]);
                }
                @Override
                public int getBatchSize() {
                    return applicationEntity.size();
                }
            });
        } else {
            String sql = "INSERT INTO "
                    .concat(nameTable)
                    .concat(" (id, text, description) VALUES (?, ?, ?)");
            template.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ApplicationEntity entity = applicationEntity.get(i);
                    ps.setInt(1, entity.getIntegers()[0]);
                    ps.setString(2, entity.getStrings()[0]);
                    ps.setString(3, entity.getStrings()[1]);
                }
                @Override
                public int getBatchSize() {
                    return applicationEntity.size();
                }
            });
        }
    }
    @Override
    public void remove(Integer id,String nameTable) {
        String sql = "DELETE FROM ".concat(nameTable).concat(" WHERE id = ?");
        template.update(sql, id);
    }
    private ApplicationEntity loadData(String query, String nameTable, ApplicationEntity entity){
        if (checkNameTable(nameTable)) {
            template.update(query,
                    entity.getIntegers()[0],
                    entity.getIntegers()[1],
                    entity.getIntegers()[2],
                    entity.getStrings()[0],
                    entity.getStrings()[1],
                    entity.getStrings()[2]);
        } else {
            template.update(query,
                    entity.getIntegers()[0],
                    entity.getStrings()[0],
                    entity.getStrings()[1]);
        }
        return entity;
    }
    private boolean checkNameTable(String nameTable) {
        return nameTable.contains("books");
    }
    public Integer generateId(String nameTable) {
        int id;
        AtomicInteger integer;
        HashSet<Integer> checkId;
        if (checkNameTable(nameTable)) {
            checkId = idBook;
        } else {
           checkId = idCategory;
        }
        integer = new AtomicInteger(checkId.size());
        do {
            id = integer.getAndIncrement();
        } while (checkId.contains(id));
        return id;
    }
}
