DROP TABLE test1;

CREATE TABLE "user"(
    id BIGINT PRIMARY KEY,
    username VARCHAR (50) UNIQUE NOT NULL,
    password VARCHAR NOT NULL,
    firstname VARCHAR (50) NOT NULL,
    lastname VARCHAR (50) NOT NULL,
    role VARCHAR (50) NOT NULL
);

