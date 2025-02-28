CREATE TABLE tb_news(
    id BIGINT auto_increment primary key,
    icon VARCHAR(255) not null,
    description VARCHAR(255) not null,
    id_user BIGINT,
    FOREIGN KEY(id_user) REFERENCES tb_users(id)
);

CREATE TABLE tb_features(
  id BIGINT auto_increment primary key,
  icon VARCHAR(255) not null,
  description VARCHAR(255) not null,
  id_user BIGINT,
  FOREIGN KEY(id_user) REFERENCES tb_users(id)
);
