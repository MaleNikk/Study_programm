--Create schema
CREATE SCHEMA IF NOT EXISTS webflux_schema;

--create table 'users'

CREATE TABLE IF NOT EXISTS webflux_schema.app_user
 (
id BIGINT PRIMARY KEY,
user_name VARCHAR(128) NOT NULL UNIQUE,
first_name VARCHAR(128) NOT NULL,
last_name VARCHAR(128) NOT NULL,
password VARCHAR(1024) NOT NULL,
role VARCHAR(32) NOT NULL,
date_create DATE,
date_update DATE,
enable BOOLEAN NOT NULL DEFAULT FALSE
);
