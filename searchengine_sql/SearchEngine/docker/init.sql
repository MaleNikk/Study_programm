CREATE SCHEMA IF NOT EXISTS storage;

CREATE TABLE IF NOT EXISTS storage.bad_urls
(
    id BIGINT PRIMARY KEY,
    parentUrl VARCHAR(350) NOT NULL,
    name VARCHAR(255) NOT NULL,
    url VARCHAR(350) NOT NULL
);

CREATE TABLE IF NOT EXISTS storage.sys_urls
(
    id BIGINT PRIMARY KEY,
    parentUrl VARCHAR(350) NOT NULL,
    name VARCHAR(255) NOT NULL,
    url VARCHAR(350) NOT NULL
);

CREATE TABLE IF NOT EXISTS storage.all_urls
(
    id BIGINT PRIMARY KEY,
    parentUrl VARCHAR(350) NOT NULL,
    name VARCHAR(255) NOT NULL,
    url VARCHAR(350) NOT NULL
);

CREATE TABLE IF NOT EXISTS storage.find_urls
(
    id BIGINT PRIMARY KEY,
    parentUrl VARCHAR(350) NOT NULL,
    name VARCHAR(255) NOT NULL,
    url VARCHAR(350) NOT NULL
);

CREATE TABLE IF NOT EXISTS storage.parent_sites
(
    id BIGINT PRIMARY KEY,
    url VARCHAR(350) NOT NULL,
    name VARCHAR(350) NOT NULL,
    createdTime VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    statusTime BIGINT NOT NULL,
    error VARCHAR(255) NOT NULL,
    pages BIGINT DEFAULT NULL,
    lemmas BIGINT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS storage.words
(
    id SERIAL PRIMARY KEY ,
    lemma VARCHAR(50) NOT NULL,
    word VARCHAR(50) NOT NULL,
    url VARCHAR(350) NOT NULL,
    name VARCHAR(350) NOT NULL,
    parentUrl VARCHAR(350) NOT NULL
);

CREATE INDEX index_for_lemma ON storage.words (lemma);