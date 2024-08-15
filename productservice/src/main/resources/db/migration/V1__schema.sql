DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    id SERIAL PRIMARY KEY,
    username character varying(100)
)
DROP TABLE IF EXISTS products;
CREATE TABLE products (
                         id SERIAL PRIMARY KEY,
                         account VARCHAR(20),
                         balance DECIMAL(10, 2),
                         product_type VARCHAR(10),
                         user_id INTEGER,
                         FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO users (id, username) VALUES (1,'user1'),(2,'user2');

INSERT INTO products (account, balance, product_type, user_id)
VALUES
    ('account1', 100.00, 'type1', (SELECT id FROM users WHERE username = 'user1')),
    ('account2', 200.00, 'type2', (SELECT id FROM users WHERE username = 'user1')),
    ('account3', 300.00, 'type3', (SELECT id FROM users WHERE username = 'user2')),
    ('account4', 400.00, 'type4', (SELECT id FROM users WHERE username = 'user2'));
