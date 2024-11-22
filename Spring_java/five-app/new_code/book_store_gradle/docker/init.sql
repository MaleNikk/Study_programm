-- Создание схемы bookstore_schema
CREATE SCHEMA IF NOT EXISTS bookstore_schema;

--Create table books--

CREATE TABLE IF NOT EXISTS bookstore_schema.books
(
    id BIGINT PRIMARY KEY,
    price BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    author VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    date_print VARCHAR(255) NOT NULL
);

--Create table category--

CREATE TABLE IF NOT EXISTS bookstore_schema.category
(
    id BIGINT PRIMARY KEY,
    text VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL
);

