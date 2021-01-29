CREATE TABLE account_data
(
    account_id BIGSERIAL UNIQUE,
    amount    REAL
);

INSERT INTO account_data (account_id,amount)
VALUES (132,10000),
       (2,-100),
       (3,23123123);