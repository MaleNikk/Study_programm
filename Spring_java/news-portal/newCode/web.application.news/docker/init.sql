-- Создание схемы news_schema
CREATE SCHEMA IF NOT EXISTS news_schema;

--Create table readers--
CREATE TABLE IF NOT EXISTS news_schema.readers
(
    id BIGINT PRIMARY KEY,
    pin BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL
);

--Create table commits--

CREATE TABLE IF NOT EXISTS news_schema.comments
(
    id BIGINT PRIMARY KEY,
    id_news BIGINT NOT NULL,
    pin_reader BIGINT NOT NULL,
    author VARCHAR(255) NOT NULL,
    text VARCHAR(1500) NOT NULL,
    date_comment VARCHAR(255) NOT NULL
);

--Create table categories--
CREATE TABLE IF NOT EXISTS news_schema.categories
(
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

-- Создание таблицы news в схеме news_schema
CREATE TABLE IF NOT EXISTS news_schema.news
(
    id BIGINT PRIMARY KEY,
    category_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    news VARCHAR(2500) NOT NULL,
    author VARCHAR(255) NOT NULL,
    date_news VARCHAR(255) NOT NULL
);

