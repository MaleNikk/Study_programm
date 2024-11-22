-- Создание схемы news_schema
CREATE SCHEMA IF NOT EXISTS news_schema;

--Create table categories--
CREATE TABLE IF NOT EXISTS news_schema.categories
(
    id BIGINT PRIMARY KEY,
    title VARCHAR(550) NOT NULL
);

-- Создание таблицы news в схеме news_schema
CREATE TABLE IF NOT EXISTS news_schema.news
(
    id BIGINT PRIMARY KEY,
    category_id BIGINT NOT NULL,
    title VARCHAR(550) NOT NULL,
    news VARCHAR(1500) NOT NULL,
    author VARCHAR(255) NOT NULL,
    date_news VARCHAR(255) NOT NULL
);

