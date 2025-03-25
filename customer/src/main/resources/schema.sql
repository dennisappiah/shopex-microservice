DROP TABLE IF EXISTS portfolio_item;
DROP TABLE IF EXISTS customer;

CREATE TABLE customer (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    balance INTEGER
);

CREATE TABLE portfolio_item (
    id SERIAL PRIMARY KEY,
    customer_id INTEGER,
    ticker VARCHAR(10),
    quantity INTEGER,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);