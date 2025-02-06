CREATE SCHEMA IF NOT EXISTS storage;

CREATE TABLE IF NOT EXISTS bad_urls
(
    id SERIAL PRIMARY KEY,
    parent_url VARCHAR(350) NOT NULL,
    name VARCHAR(255) NOT NULL,
    url VARCHAR(350) NOT NULL
);

CREATE TABLE IF NOT EXISTS sys_urls
(
    id SERIAL PRIMARY KEY,
    parent_url VARCHAR(350) NOT NULL,
    name VARCHAR(255) NOT NULL,
    url VARCHAR(350) NOT NULL
);

CREATE TABLE IF NOT EXISTS all_urls
(
    id SERIAL PRIMARY KEY,
    parent_url VARCHAR(350) NOT NULL,
    name VARCHAR(255) NOT NULL,
    url VARCHAR(350) NOT NULL
);

CREATE TABLE IF NOT EXISTS find_urls
(
    id SERIAL PRIMARY KEY,
    parent_url VARCHAR(350) NOT NULL,
    name VARCHAR(255) NOT NULL,
    url VARCHAR(350) NOT NULL
);

CREATE TABLE IF NOT EXISTS parent_sites
(
    id SERIAL PRIMARY KEY,
    url VARCHAR(350) NOT NULL,
    name VARCHAR(350) NOT NULL,
    created_time VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    status_time BIGINT NOT NULL,
    error VARCHAR(255) NOT NULL,
    pages BIGINT DEFAULT NULL,
    lemmas BIGINT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS words
(
    id SERIAL PRIMARY KEY ,
    lemma VARCHAR(50) NOT NULL,
    word VARCHAR(50) NOT NULL,
    url VARCHAR(350) NOT NULL,
    name VARCHAR(350) NOT NULL,
    parent_url VARCHAR(350) NOT NULL
);

CREATE INDEX index_for_lemma ON words (lemma);
CREATE INDEX index_for_words_parent ON words (parent_url);
CREATE INDEX index_for_url ON parent_sites (url);
CREATE INDEX index_for_all_parent ON all_urls (parent_url);
CREATE INDEX index_for_sys_parent ON sys_urls (parent_url);
CREATE INDEX index_for_bad_parent ON bad_urls (parent_url);
CREATE INDEX index_for_find_parent ON find_urls (parent_url);