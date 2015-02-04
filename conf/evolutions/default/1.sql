# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table video (
  id                        bigint not null,
  title                     varchar(255),
  description               varchar(255),
  thumbnail_path            varchar(255),
  duration                  integer,
  approved                  boolean,
  constraint pk_video primary key (id))
;

create sequence video_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists video;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists video_seq;

