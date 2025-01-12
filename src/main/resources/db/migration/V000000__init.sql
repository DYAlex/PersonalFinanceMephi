CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    username varchar NOT NULL,
    role varchar NOT NULL,
    password     varchar NOT NULL
);

CREATE TABLE wallets
(
    id      SERIAL PRIMARY KEY,
    user_id bigint NOT NULL,
    balance float8 default 0.0,
    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE categories
(
    id      SERIAL PRIMARY KEY,
    user_id bigint  NOT NULL,
    name    varchar NOT NULL,
    type    varchar NOT NULL,
    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE limits
(
    id          SERIAL PRIMARY KEY,
    wallet_id   bigint    NOT NULL,
    category_id bigserial NOT NULL,
    value       float8,
    CONSTRAINT fk_wallet FOREIGN KEY (wallet_id) REFERENCES wallets (id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE transactions
(
    id          SERIAL PRIMARY KEY,
    wallet_id   bigint                      NOT NULL,
    category_id bigserial                   NOT NULL,
    date        timestamp without time zone NOT NULL,
    amount      numeric                     NOT NULL,
    CONSTRAINT fk_wallet FOREIGN KEY (wallet_id) REFERENCES wallets (id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories (id)
);
