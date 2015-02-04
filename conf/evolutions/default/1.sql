# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table question (
  question_id               bigint not null,
  text                      varchar(255),
  SCHOOL                    bigint,
  constraint pk_question primary key (question_id))
;

create table school (
  school_id                 bigint not null,
  name                      varchar(255),
  constraint pk_school primary key (school_id))
;

create table video (
  id                        bigint not null,
  title                     varchar(255),
  description               varchar(255),
  thumbnail_path            varchar(255),
  duration                  integer,
  approved                  boolean,
  constraint pk_video primary key (id))
;

create sequence question_seq;

create sequence school_seq;

create sequence video_seq;

alter table question add constraint fk_question_school_1 foreign key (SCHOOL) references school (school_id) on delete restrict on update restrict;
create index ix_question_school_1 on question (SCHOOL);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists question;

drop table if exists school;

drop table if exists video;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists question_seq;

drop sequence if exists school_seq;

drop sequence if exists video_seq;

