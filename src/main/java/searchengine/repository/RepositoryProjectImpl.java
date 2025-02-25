package searchengine.repository;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import searchengine.dto.entity.*;
import searchengine.dto.mapper.RowMapperModelSite;
import searchengine.dto.mapper.RowMapperParentSite;
import searchengine.dto.mapper.RowMapperWord;
import searchengine.config.FixedValue;
import searchengine.service.ServiceBuildingDataImpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

@Repository
@Slf4j
public class RepositoryProjectImpl implements RepositoryProject {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapperModelSite mapperModelSite;

    private final RowMapperParentSite mapperParentSite;

    private final RowMapperWord mapperWord;

    @Autowired
    public RepositoryProjectImpl(JdbcTemplate jdbcTemplate,
                                 RowMapperModelSite mapperModelSite,
                                 RowMapperParentSite mapperParentSite,
                                 RowMapperWord mapperWord) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapperModelSite = mapperModelSite;
        this.mapperParentSite = mapperParentSite;
        this.mapperWord = mapperWord;
    }

    @Override
    public synchronized void saveSystemSite(ModelSite modelSite) {
        String sql = "INSERT INTO sys_urls (url, parent_url, name) VALUES(?, ?, ?);";
        jdbcTemplate.update(sql, modelSite.url(), modelSite.parentUrl(), modelSite.name());
    }

    @Override
    public synchronized void saveBadSite(ModelSite modelSite) {
        if (Objects.equals(modelSite.url(), modelSite.parentUrl())) {
            ModelParentSite parentSite = takeParentSiteByUrl(modelSite.parentUrl());
            saveStatistics(parentSite.getUrl(), parentSite.getLemmas(), parentSite.getPages(), FixedValue.FAILED);
        }
        String sql = "INSERT INTO bad_urls (url, parent_url, name) VALUES(?, ?, ?);";
        jdbcTemplate.update(sql, modelSite.url(), modelSite.parentUrl(), modelSite.name());
    }

    @Override
    public synchronized void saveWords(String data) {
        String sql = "INSERT INTO words (lemma, url, parent_url, name, frequency) VALUES ";
        jdbcTemplate.update(sql.concat(data).concat(";"));
    }

    @Override
    public synchronized void saveFoundSites(List<ModelSite> foundSites) {
        List<ModelSite> forSave = foundSites.stream()
                .filter(this::checkSavedFoundSite)
                .filter(modelSite -> checkSavedAllSite(modelSite.url())).toList();
        if (!forSave.isEmpty()) {
            String sql = "INSERT INTO find_urls (url, parent_url, name) VALUES(?, ?, ?);";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                    ModelSite modelSite = forSave.get(i);
                    ps.setString(1, modelSite.url());
                    ps.setString(2, modelSite.parentUrl());
                    ps.setString(3, modelSite.name());
                }
                @Override
                public int getBatchSize() {
                    return forSave.size();
                }
            });
        }
    }

    @Override
    public synchronized void saveParentSites(List<ModelParentSite> parentSites) {
        List<ModelParentSite> siteForSave = parentSites.stream().filter(this::checkSavedParentSite).toList();
        if (!siteForSave.isEmpty()) {
            String sql = "INSERT INTO parent_sites (url, name, created_time, status, status_time, error, pages, lemmas)"
                    .concat(" VALUES(?, ?, ?, ?, ?, ?, ?, ?);");
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                    ModelParentSite parentSite = siteForSave.get(i);
                    ps.setString(1, parentSite.getUrl());
                    ps.setString(2, parentSite.getName());
                    ps.setString(3, parentSite.getCreatedTime());
                    ps.setString(4, parentSite.getStatus());
                    ps.setLong(5, parentSite.getStatusTime());
                    ps.setString(6, parentSite.getError());
                    ps.setInt(7, parentSite.getPages());
                    ps.setInt(8, parentSite.getLemmas());
                }
                @Override
                public int getBatchSize() {
                    return siteForSave.size();
                }
            });
            siteForSave.forEach(site -> delete(site.getUrl()));
        }
    }

    @Override
    public synchronized void saveStatistics(String parentUrl, Integer lemmas, Integer pages, String status) {
        ModelParentSite site = checkStatus(takeParentSiteByUrl(parentUrl));
        String sql =
                "UPDATE parent_sites SET status = ?,status_time = ?,error = ?,pages = ?,lemmas = ? WHERE url = ?;";
        jdbcTemplate.update(sql, status, System.currentTimeMillis(), site.getError(),
                (pages + site.getPages()), (lemmas + site.getLemmas()), parentUrl);
    }

    @Override
    public synchronized List<ModelSite> takeFoundSites() {
        String query = "SELECT * FROM find_urls LIMIT 50";
        List<ModelSite> sites = jdbcTemplate.query(query, mapperModelSite);
        if (!sites.isEmpty()) {
            saveRegisteredSites(sites);
            HashSet<String> urls = new HashSet<>(sites.stream().map(ModelSite::url).toList());
            for (String url : urls) {
                String sql = "DELETE FROM find_urls WHERE url = ?";
                jdbcTemplate.update(sql, url);
            }
        } else {
            saveFoundSites(takeParentSites().stream().map(site ->
                    new ModelSite(site.getUrl(), site.getUrl(), site.getName())).toList());
        }
        return sites;
    }

    @Override
    public List<ModelWord> takeModelWords(String lemma, String url, int limit) {
        String query = "SELECT * FROM words WHERE  lemma = '".concat(lemma).concat("' LIMIT ")
                .concat(String.valueOf(limit + 25)).concat(";");
        if (url != null) {
            query = "SELECT * FROM words WHERE lemma = '".concat(lemma).concat("' AND parent_url = '")
                    .concat(url).concat("' LIMIT ").concat(String.valueOf(limit + 25)).concat(";");
        }
        return jdbcTemplate.query(query, mapperWord);
    }

    @Override
    public List<ModelWord> showIndexedWords() {
        String query = "SELECT * FROM words LIMIT 50;";
        return jdbcTemplate.query(query, mapperWord);
    }

    @Override
    public List<ModelSite> showIndexedSites() {
        String query = "SELECT * FROM all_urls LIMIT 50;";
        return jdbcTemplate.query(query, mapperModelSite);
    }

    @Override
    public List<ModelParentSite> takeParentSites() {
        String query = "SELECT * FROM parent_sites;";
        return jdbcTemplate.query(query, mapperParentSite).stream().map(this::checkStatus).toList();
    }

    @Override
    public synchronized void delete(String parentUrl) {
        String[] queries = {
                "DELETE FROM all_urls WHERE parent_url = ?;",
                "DELETE FROM find_urls WHERE parent_url = ?;",
                "DELETE FROM bad_urls WHERE parent_url = ?;",
                "DELETE FROM sys_urls WHERE parent_url = ?;",
                "DELETE FROM words WHERE parent_url = ?;"
        };
        for (String query : queries) {
            jdbcTemplate.update(query, parentUrl);
        }
        saveFoundSites(Stream.of(takeParentSiteByUrl(parentUrl))
                .map(site -> new ModelSite(site.getUrl(), site.getUrl(), site.getName())).toList());
    }

    private void saveRegisteredSites(List<ModelSite> foundSites) {
        List<ModelSite> forSave = foundSites.stream()
                .filter(modelSite -> checkSavedAllSite(modelSite.url())).toList();
        if (!forSave.isEmpty()) {
            String sql = "INSERT INTO all_urls (url, parent_url, name) VALUES(?, ?, ?);";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                    ModelSite modelSite = forSave.get(i);
                    ps.setString(1, modelSite.url());
                    ps.setString(2, modelSite.parentUrl());
                    ps.setString(3, modelSite.name());

                }
                @Override
                public int getBatchSize() {
                    return forSave.size();
                }
            });
        }
    }

    @Override
    public ModelParentSite takeParentSiteByUrl(String parentUrl) {
        String query = "SELECT * FROM parent_sites WHERE url = '".concat(parentUrl).concat("' LIMIT 1;");
        return jdbcTemplate.query(query, mapperParentSite).get(FixedValue.ZERO);
    }

    private boolean checkSavedParentSite(ModelParentSite modelParentSite) {
        String query = "SELECT * FROM parent_sites WHERE url = '".concat(modelParentSite.getUrl()).concat("' LIMIT 1;");
        return jdbcTemplate.query(query, mapperParentSite).isEmpty();
    }

    private boolean checkSavedAllSite(String url) {
        String query = "SELECT * FROM all_urls WHERE url = '".concat(url).concat("' LIMIT 1;");
        return jdbcTemplate.query(query, mapperModelSite).isEmpty();
    }

    private boolean checkSavedFoundSite(ModelSite modelSite) {
        String query = "SELECT * FROM find_urls WHERE url = '".concat(modelSite.url()).concat("' LIMIT 1;");
        return jdbcTemplate.query(query, mapperModelSite).isEmpty();
    }

    private boolean checkIndexedSite(String parentUrl) {
        String query = "SELECT * FROM find_urls WHERE parent_url = '".concat(parentUrl).concat("' LIMIT 1;");
        return jdbcTemplate.query(query, mapperModelSite).isEmpty();
    }

    private boolean checkBadSite(String parentUrl) {
        String query = "SELECT * FROM bad_urls WHERE url = '".concat(parentUrl).concat("' LIMIT 1;");
        return jdbcTemplate.query(query, mapperModelSite).isEmpty();
    }

    private ModelParentSite checkStatus(ModelParentSite site) {
        String url = site.getUrl();
        if (!checkBadSite(url)) {
            site.setStatus(FixedValue.FAILED);
            site.setError(FixedValue.FAILED_ERROR);
        } else if (checkIndexedSite(url) && checkBadSite(url) && !checkSavedAllSite(url)) {
            site.setStatus(FixedValue.INDEXING_COMPLETE);
        } else if (ServiceBuildingDataImpl.START.get()) {
            site.setStatus(FixedValue.IN_PROGRESS);
        } else if (checkIndexedSite(url) && checkSavedAllSite(url)) {
            site.setStatus(FixedValue.INDEXING_NOT_STARTED);
        } else {
            site.setStatus(FixedValue.INDEXED_STOP);
        }
        return site;
    }
}
