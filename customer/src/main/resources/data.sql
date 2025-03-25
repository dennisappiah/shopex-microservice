INSERT INTO customer(name, balance)
VALUES
    ('kojo', 10000),
    ('Owusu', 10000),
    ('Mary', 10000);

INSERT INTO portfolio_item(customer_id, ticker, quantity)
VALUES
    (1, 'AAPL', 10),
    (2, 'GOOGL', 5);