package searchengine.searching.repository;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Component;
import searchengine.dto.entity.*;
import searchengine.dto.mapper.RowMapperModelSite;
import searchengine.dto.mapper.RowMapperParentSite;
import searchengine.dto.mapper.RowMapperWord;
import searchengine.searching.processing.FixedValue;
import searchengine.searching.processing.ProjectMorphology;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Component
@Slf4j
public final class ManagementRepository implements AppManagementRepositoryImpl {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapperModelSite mapperModelSite;

    private final RowMapperParentSite mapperParentSite;

    private final RowMapperWord mapperWord;

    private final ProjectMorphology morphology = new ProjectMorphology();

    @Autowired
    public ManagementRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapperModelSite = new RowMapperModelSite();
        this.mapperParentSite = new RowMapperParentSite();
        this.mapperWord = new RowMapperWord();
    }

    @Override
    public synchronized void saveSystemSite(ModelSite modelSite) {
        String sql = "INSERT INTO sys_urls (parent_url, name,url) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, modelSite.parentUrl(), modelSite.name(), modelSite.url());
    }

    @Override
    public synchronized void saveBadSite(ModelSite modelSite) {
        String sql = "INSERT INTO bad_urls (parent_url, name,url) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, modelSite.parentUrl(), modelSite.name(), modelSite.url());
    }

    @Override
    public synchronized void saveWord(List<ModelWord> modelWords) {
        for (ModelWord modelWord : modelWords) {
            String sql = "INSERT INTO words (lemma, word, url, name, parent_url) VALUES(?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    modelWord.lemma(),
                    modelWord.word(),
                    modelWord.url(),
                    modelWord.name(),
                    modelWord.parentUrl()
            );
        }
    }

    @Override
    public synchronized void saveFoundSites(List<ModelSite> foundSites) {
        List<ModelSite> forSave = foundSites.stream()
                .filter(this::checkSavedFoundSite)
                .filter(this::checkSavedAllSite)
                .filter(modelSite -> modelSite.url().length() < 350).toList();
        if (!forSave.isEmpty()) {
            String sql = "INSERT INTO find_urls (parent_url, name, url) VALUES(?, ?, ?)";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                    ModelSite modelSite = forSave.get(i);
                    ps.setString(1, modelSite.parentUrl());
                    ps.setString(2, modelSite.name());
                    ps.setString(3, modelSite.url());
                }

                @Override
                public int getBatchSize() {
                    return forSave.size();
                }
            });
        }
    }

    @Override
    public void saveParentSites(List<ModelParentSite> parentSites) {
        List<ModelParentSite> siteForSave = parentSites
                .stream().filter(this::checkSavedParentSite).toList();
        if (!siteForSave.isEmpty()) {
            String sql = "INSERT INTO parent_sites (url, name, created_time, status, status_time, error, pages, lemmas)"
                    .concat("VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                    ModelParentSite parentSite = siteForSave.get(i);
                    ps.setString(1, parentSite.url());
                    ps.setString(2, parentSite.name());
                    ps.setString(3, parentSite.createdTime());
                    ps.setString(4, parentSite.status());
                    ps.setLong(5, parentSite.statusTime());
                    ps.setString(6, parentSite.error());
                    ps.setInt(7, parentSite.pages());
                    ps.setInt(8, parentSite.lemmas());
                }

                @Override
                public int getBatchSize() {
                    return siteForSave.size();
                }
            });
        }
    }

    @Override
    public synchronized void saveStatistics(String parentUrl, Integer lemmas, Integer pages, String status) {
        ModelParentSite parentSite = findParentSiteByUrl(parentUrl);
        if (parentSite != null) {
            String sql = "UPDATE parent_sites SET status = ?, status_time = ?, pages = ?, lemmas = ? WHERE url = ?";
            jdbcTemplate.update(sql, status, System.currentTimeMillis(), (pages + parentSite.pages()),
                    (lemmas + parentSite.lemmas()), parentUrl);
        }
    }

    @Override
    public synchronized ModelSite getFoundSite() {
        String query = "SELECT * FROM find_urls ";
        List<ModelSite> sites = jdbcTemplate.query(query, mapperModelSite);
        ModelSite result = null;
        if (!sites.isEmpty()) {
            result = sites.get(FixedValue.ZERO);
            String sqlSave = "INSERT INTO all_urls (parent_url, name,url) VALUES(?, ?, ?)";
            jdbcTemplate.update(sqlSave,
                    result.parentUrl(),
                    result.name(),
                    result.url());
            String sql = "DELETE FROM find_urls WHERE url = ?";
            jdbcTemplate.update(sql, result.url());
        }
        return result;
    }

    @Override
    public synchronized Integer countFoundSites() {
        String sql = "SELECT * FROM find_urls";
        List<ModelSite> find = jdbcTemplate.query(sql, mapperModelSite);
        return find.size();
    }

    public List<ModelWord> findModelWords(String word) {
        String query = "EMPTY";
        if (!word.isBlank()) {
            query = "SELECT * FROM words WHERE lemma = '".concat(
                    morphology.getForm(word)).concat("'");
        }
        return query.equals("EMPTY") ? List.of() : jdbcTemplate.query(query, mapperWord);
    }

    @Override
    public List<ModelWord> showIndexedWords() {
        String query = "SELECT * FROM words ";
        return jdbcTemplate.query(query, mapperWord).stream().limit(50).toList();
    }

    @Override
    public List<ModelSite> showIndexedSites() {
        String query = "SELECT * FROM all_urls ";
        return jdbcTemplate.query(query, mapperModelSite);
    }

    @Override
    public List<ModelParentSite> getParentSites() {
        String query = "SELECT * FROM parent_sites";
        return jdbcTemplate.query(query, mapperParentSite);
    }

    @Override
    public void delete(String parentUrl) {
        String[] queries = {
                "DELETE FROM all_urls WHERE parent_url = ? ",
                "DELETE FROM find_urls WHERE parent_url = ?",
                "DELETE FROM bad_urls WHERE parent_url = ?",
                "DELETE FROM sys_urls WHERE parent_url = ?",
                "DELETE FROM words WHERE parent_url = ?",
                "DELETE FROM parent_sites WHERE url = ?"
        };
        for (String query : queries) {
            jdbcTemplate.update(query, parentUrl);
        }
    }

    private boolean checkSavedParentSite(ModelParentSite modelParentSite) {
        String query = "SELECT * FROM parent_sites WHERE url = ?";
        ModelParentSite parentSite = DataAccessUtils.singleResult(
                jdbcTemplate.query(query,
                        new ArgumentPreparedStatementSetter(new Object[]{modelParentSite.url()}),
                        new RowMapperResultSetExtractor<>(mapperParentSite, 2)));
        return Objects.equals(parentSite, null);
    }

    private boolean checkSavedAllSite(ModelSite modelSite) {
        String query = "SELECT * FROM all_urls WHERE url = ?";
        ModelSite result = DataAccessUtils.singleResult(
                jdbcTemplate.query(query,
                        new ArgumentPreparedStatementSetter(new Object[]{modelSite.url()}),
                        new RowMapperResultSetExtractor<>(mapperModelSite, 4)));
        return Objects.equals(result, null);
    }

    private boolean checkSavedFoundSite(ModelSite modelSite) {
        String query = "SELECT * FROM find_urls WHERE url = ?";
        ModelSite result = DataAccessUtils.singleResult(
                jdbcTemplate.query(query,
                        new ArgumentPreparedStatementSetter(new Object[]{modelSite.url()}),
                        new RowMapperResultSetExtractor<>(mapperModelSite, 4)));
        return Objects.equals(result, null);
    }

    private ModelParentSite findParentSiteByUrl(String parentUrl) {
        String query = "SELECT * FROM parent_sites WHERE url = ?";
        return DataAccessUtils.singleResult(
                jdbcTemplate.query(query,
                        new ArgumentPreparedStatementSetter(new Object[]{parentUrl}),
                        new RowMapperResultSetExtractor<>(mapperParentSite, 2)));
    }
}
