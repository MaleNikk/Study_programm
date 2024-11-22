-- Создание схемы news_schema
CREATE SCHEMA IF NOT EXISTS user_schema;

--Create table app_user--
CREATE TABLE IF NOT EXISTS user_schema.app_user
(
    id BIGINT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

--Create table app_user--
CREATE TABLE IF NOT EXISTS user_schema.user_roles
(
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role VARCHAR(255) NOT NULL
);




