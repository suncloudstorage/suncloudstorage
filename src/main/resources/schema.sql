DROP TABLE if exists users;

create table users
(
    id         bigint not null auto_increment,
    email      varchar(255),
    first_name varchar(255),
    last_name  varchar(255),
    password   varchar(255),
    role       varchar(255),
    status     varchar(255),
    username   varchar(255),
    primary key (id)
)
