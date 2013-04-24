# --- First database schema

# --- !Ups

create table user (
  email                     varchar(255) not null primary key,
  name                      varchar(255) not null,
  password                  varchar(255) not null
);

create table feed (
  id                        bigint not null primary key,
  name                      varchar(255) not null,
  description               varchar(500),
  anon                      boolean,
  owner                     varchar(255) not null,
  foreign key(owner)        references user(name) on delete cascade
);

create sequence feed_seq start with 1000;
create sequence element_seq start with 1000;
create sequence cmment_seq start with 1000;

create table element (
  id                        bigint not null primary key,
  feedId                    bigint,
  name                      varchar(100),
  description               varchar(1000),
  kind                      varchar(50),
  attrb                     varchar(3000),
  foreign key(feedId)       references feed(id) on delete cascade
);

create table cmment ( 
  id                        bigint not null primary key,
  elementId                 bigint,
  value                     varchar(500),
  kind                      varchar(500),
  author                    varchar(255),
  foreign key(elementId)    references element(id) on delete cascade,
  foreign key(author)       references user(name) on delete cascade
);

# --- !Downs

drop table if exists cmment;
drop sequence if exists cmment_seq;
drop table if exists element;
drop sequence if exists element_seq;
drop table if exists feed;
drop sequence if exists feed_seq;
drop table if exists user;
