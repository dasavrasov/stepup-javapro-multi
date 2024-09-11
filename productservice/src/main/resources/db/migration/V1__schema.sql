DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    username VARCHAR(100)
);

CREATE TABLE products (
                          id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                          account VARCHAR(20),
                          balance NUMERIC(14,2),
                          product_type VARCHAR(10),
                          user_id INTEGER
);

ALTER TABLE products ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id);

INSERT INTO users (username) VALUES ('user1'),('user2');

INSERT INTO products (account, balance, product_type, user_id)
VALUES
    ('account1', 100.00, '0', (SELECT id FROM users WHERE username = 'user1')),
    ('account2', 200.00, '0', (SELECT id FROM users WHERE username = 'user1')),
    ('account3', 300.00, '1', (SELECT id FROM users WHERE username = 'user2')),
    ('account4', 400.00, '1', (SELECT id FROM users WHERE username = 'user2'));