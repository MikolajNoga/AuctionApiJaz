CREATE TABLE section(
    id INT NOT NULL,
    name VARCHAR(100) NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE category(
    id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    section_id INT NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT section_fk FOREIGN KEY (section_id) REFERENCES section(id)
);

CREATE TABLE auction(
    id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    author_id INT NOT NULL,
    category_id INT NOT NULL,
    version INT NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT category_fk FOREIGN KEY (category_id) REFERENCES category (id),
    CONSTRAINT author_fk FOREIGN KEY (author_id) REFERENCES "user" (id)
);

CREATE TABLE auction_photo(
    id INT NOT NULL,
    name TEXT NOT NULL,
    position INT NOT NULL,
    auction_id INT NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT auction_fk FOREIGN KEY (auction_id) REFERENCES auction (id)
);

CREATE TABLE parameter(
    id INT NOT NULL,
    key VARCHAR(50),

    PRIMARY KEY (id)
);

CREATE TABLE auction_parameter(
    id INT NOT NULL,
    value VARCHAR(50) NOT NULL,
    auction_id INT NOT NULL,
    parameter_id INT NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT parameter_fk FOREIGN KEY (parameter_id) REFERENCES parameter (id),
    CONSTRAINT auction_fk FOREIGN KEY (auction_id) REFERENCES auction (id)
);