 CREATE TABLE tb_users(
    id BIGINT auto_increment primary key,
    name VARCHAR(50) not null,
    id_account BIGINT,
    id_card BIGINT,
    FOREIGN KEY(id_account) REFERENCES tb_account(id),
    FOREIGN KEY(id_card) REFERENCES tb_card(id)
 );
