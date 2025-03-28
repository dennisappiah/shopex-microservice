DROP TABLE IF EXISTS portfolio_item;
DROP TABLE IF EXISTS customer;

CREATE TABLE customer (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    balance INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE portfolio_item (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    ticker VARCHAR(10) NOT NULL,
    quantity INTEGER NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE,
    UNIQUE (customer_id, ticker)
);