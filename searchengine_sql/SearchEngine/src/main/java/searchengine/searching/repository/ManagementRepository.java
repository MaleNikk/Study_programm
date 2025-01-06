package searchengine.searching.repository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import searchengine.dto.entity.*;
import searchengine.dto.mapper.RowMapperModelSite;
import searchengine.dto.mapper.RowMapperParentSite;
import searchengine.dto.mapper.RowMapperWord;
import searchengine.searching.processing.FixedValue;
import searchengine.searching.processing.ProjectMorphology;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Data
@Slf4j
public class ManagementRepository implements AppManagementRepositoryImpl {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapperModelSite mapperModelSite = new RowMapperModelSite();

    private final RowMapperParentSite mapperParentSite = new RowMapperParentSite();

    private final RowMapperWord mapperWord = new RowMapperWord();

    private final AtomicLong number = new AtomicLong(FixedValue.ZERO);

    @Override
    public synchronized void saveSystemSite(ModelSite modelSite) {
        String sql = "INSERT INTO sys_urls (id, parentUrl, name,url) VALUES(?, ?, ?, ?)";
        getJdbcTemplate().update(sql,modelSite.url().hashCode(),modelSite.parentUrl(), modelSite.name(),modelSite.url());
    }

    @Override
    public synchronized void saveBadSite(ModelSite modelSite) {
        String sql = "INSERT INTO bad_urls (id, parentUrl, name,url) VALUES(?, ?, ?, ?)";
        getJdbcTemplate().update(sql,modelSite.url().hashCode(),modelSite.parentUrl(), modelSite.name(),modelSite.url());
    }

    @Override
    public synchronized void saveWord(String word, ModelSite modelSite) {
        String sql = "INSERT INTO words (lemma, word, url, name, parentUrl) VALUES(?, ?, ?, ?, ?)";
        getJdbcTemplate().update(sql,
                ProjectMorphology.getForm(word),
                word,
                modelSite.url(),
                modelSite.name(),
                modelSite.parentUrl()
               );
    }

    @Override
    public synchronized void saveFoundSites(List<ModelSite> foundSites) {
        List<ModelSite> forSave = foundSites.stream()
                .filter(this::checkSavedFoundSite)
                .filter(this::checkSavedAllSite)
                .filter(modelSite -> modelSite.url().length() < 350).toList();
        String sql = "INSERT INTO find_urls (id, parentUrl, name, url) VALUES(?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ModelSite modelSite = forSave.get(i);
                ps.setInt(1, modelSite.url().hashCode());
                ps.setString(2, modelSite.parentUrl());
                ps.setString(3, modelSite.name());
                ps.setString(4, modelSite.url());
            }
            @Override
            public int getBatchSize() {
                return forSave.size();
            }
        });

    }

    @Override
    public void saveParentSites(List<ModelParentSite> parentSites) {
        List<ModelParentSite> siteForSave = parentSites
                .stream().filter(this::checkSavedParentSite).toList();
        String sql = "INSERT INTO parent_sites (id, url, name, createdTime, status, statusTime, error, pages, lemmas) "
                .concat("VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ModelParentSite parentSite = siteForSave.get(i);
                ps.setInt(1, parentSite.getUrl().hashCode());
                ps.setString(2, parentSite.getUrl());
                ps.setString(3, parentSite.getName());
                ps.setString(4, parentSite.getCreatedTime());
                ps.setString(5, parentSite.getStatus());
                ps.setLong(6, parentSite.getStatusTime());
                ps.setString(7, parentSite.getError());
                ps.setInt(8, parentSite.getPages());
                ps.setInt(9, parentSite.getLemmas());
            }
            @Override
            public int getBatchSize() {
                return siteForSave.size();
            }
        });
    }

    @Override
    public synchronized void saveStatistics(String parentUrl, Integer lemmas, Integer pages, String status) {
        ModelParentSite parentSite = getParentSite(parentUrl);
        if (parentSite != null) {
            String sql = "UPDATE parent_sites SET status = ?, statusTime = ?, pages = ?, lemmas = ? WHERE id = ?";
            getJdbcTemplate().update(sql, status, System.currentTimeMillis(), (pages + parentSite.getPages()),
                    (lemmas + parentSite.getLemmas()), parentUrl.hashCode());
        }
    }

    @Override
    public synchronized ModelSite getFoundSite() {
        String query = "SELECT * FROM find_urls ";
        List<ModelSite> sites = getJdbcTemplate().query(query,getMapperModelSite());
        ModelSite result = null;
        if (!sites.isEmpty()) {
            result = sites.get(FixedValue.ZERO);
            String sqlSave = "INSERT INTO all_urls (id, parentUrl, name,url) VALUES(?, ?, ?, ?)";
            getJdbcTemplate().update(sqlSave,
                    result.url().hashCode(),
                    result.parentUrl(),
                    result.name(),
                    result.url());
            String sql = "DELETE FROM find_urls WHERE id = ?";
            jdbcTemplate.update(sql, result.url().hashCode());
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
        String query = "SELECT * FROM words WHERE lemma = '".concat(
                ProjectMorphology.getForm(word)).concat("'");
        return getJdbcTemplate().query(query,getMapperWord());
    }

    @Override
    public List<ModelWord> showIndexedWords() {
        String query = "SELECT * FROM words ";
        List<ModelWord> words = new ArrayList<>(List.of());
        words.addAll(getJdbcTemplate().query(query,getMapperWord()));
        return words.subList(FixedValue.ZERO,FixedValue.COUNT_SITES);
    }

    @Override
    public List<ModelSite> showIndexedSites() {
        String query = "SELECT * FROM all_urls ";
        return getJdbcTemplate().query(query,getMapperModelSite());
    }

    @Override
    public List<ModelParentSite> getParentSites() {
        String query = "SELECT * FROM parent_sites";
        return getJdbcTemplate().query(query,getMapperParentSite());
    }

    @Override
    public void delete(String parentUrl){
        String query1 = "DELETE FROM all_urls WHERE parentUrl = ? ";
        String query2 = "DELETE FROM find_urls WHERE parentUrl = ?";
        String query3 = "DELETE FROM bad_urls WHERE parentUrl = ?";
        String query4 = "DELETE FROM sys_urls WHERE parentUrl = ?";
        String query5 = "DELETE FROM words WHERE parentUrl = ?";
        String query6 = "DELETE FROM parent_sites WHERE id = ?";
        getJdbcTemplate().update(query1,parentUrl);
        getJdbcTemplate().update(query2,parentUrl);
        getJdbcTemplate().update(query3,parentUrl);
        getJdbcTemplate().update(query4,parentUrl);
        getJdbcTemplate().update(query5,parentUrl);
        getJdbcTemplate().update(query6,parentUrl.hashCode());

    }

    private boolean checkSavedParentSite(ModelParentSite modelParentSite){
        String query = "SELECT * FROM parent_sites WHERE id = ?";
        ModelParentSite parentSite = DataAccessUtils.singleResult(
                jdbcTemplate.query(query,
                        new ArgumentPreparedStatementSetter(new Object[]{modelParentSite.getUrl().hashCode()}),
                        new RowMapperResultSetExtractor<>(getMapperParentSite(), 1)));
        return Objects.equals(parentSite, null);
    }

    private boolean checkSavedAllSite(ModelSite modelSite){
        String query = "SELECT * FROM all_urls WHERE id = ?";
        ModelSite result = DataAccessUtils.singleResult(
                jdbcTemplate.query(query,
                        new ArgumentPreparedStatementSetter(new Object[]{modelSite.url().hashCode()}),
                        new RowMapperResultSetExtractor<>(getMapperModelSite(), 1)));
        return Objects.equals(result, null);
    }

    private boolean checkSavedFoundSite(ModelSite modelSite){
        String query = "SELECT * FROM find_urls WHERE id = ?";
        ModelSite result = DataAccessUtils.singleResult(
                jdbcTemplate.query(query,
                        new ArgumentPreparedStatementSetter(new Object[]{modelSite.url().hashCode()}),
                        new RowMapperResultSetExtractor<>(getMapperModelSite(), 1)));
        return Objects.equals(result, null);
    }

    private ModelParentSite getParentSite(String parentUrl){
        String query = "SELECT * FROM parent_sites WHERE id = ?";
        return DataAccessUtils.singleResult(
                jdbcTemplate.query(query,
                        new ArgumentPreparedStatementSetter(new Object[]{parentUrl.hashCode()}),
                        new RowMapperResultSetExtractor<>(getMapperParentSite(), 1)));
    }
}
