-- Создание схемы tasks_schema
CREATE SCHEMA IF NOT EXISTS news_schema;

-- Создание таблицы task в схеме tasks_schema
CREATE TABLE IF NOT EXISTS news_schema.news
(
    id BIGINT PRIMARY KEY,
    title VARCHAR(550) NOT NULL,
    news VARCHAR(1500) NOT NULL,
    author VARCHAR(255) NOT NULL,
    date_news VARCHAR(255) NOT NULL
);