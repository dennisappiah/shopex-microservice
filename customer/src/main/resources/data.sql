-- First insert customers with explicit UUIDs
INSERT INTO customer(id, name, balance)
VALUES
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'kojo', 10000),
    ('b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'Owusu', 10000),
    ('c2eebc99-9c0b-4ef8-bb6d-6bb9bd380a33', 'Mary', 10000);

-- Then insert portfolio items using the same UUIDs
INSERT INTO portfolio_item(id, customer_id, ticker, quantity)
VALUES
    ('d3eebc99-9c0b-4ef8-bb6d-6bb9bd380a44', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'APPLE', 10),
    ('e4eebc99-9c0b-4ef8-bb6d-6bb9bd380a55', 'b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'GOOGLE', 5);