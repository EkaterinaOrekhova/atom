begin;

create schema if not exists chat;

create table if not exists chat.user (
  id    serial             not null,
  login varchar(20) unique not null,

  primary key (id)
);

create table if not exists chat.message (
  id     serial       not null,
  user_id integer     not null references chat.user on delete cascade,
  time   timestamp    not null,
  value  varchar(140) not null,

  primary key (id)
);

commit;