ALTER TABLE tb_users
ADD COLUMN username VARCHAR(50) not null AFTER name;

ALTER TABLE tb_users
ADD COLUMN password VARCHAR(150) not null AFTER username;