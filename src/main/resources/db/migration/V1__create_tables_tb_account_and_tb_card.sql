CREATE TABLE tb_account(
    id BIGINT auto_increment primary key,
    number VARCHAR(13),
    agency VARCHAR(4),
    balance DECIMAL(13,2),
    limit_value DECIMAL(13,2)
);

CREATE TABLE tb_card(
    id BIGINT auto_increment primary key,
    number VARCHAR(16),
    limit_value DECIMAL(13,2)
);