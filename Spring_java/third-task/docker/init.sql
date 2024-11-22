-- Создание схемы tasks_schema
CREATE SCHEMA IF NOT EXISTS contacts_schema;

-- Создание таблицы task в схеме tasks_schema
CREATE TABLE IF NOT EXISTS contacts_schema.contact
(
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL
);