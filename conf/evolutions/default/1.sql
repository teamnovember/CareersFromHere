# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table question (
  question_id               bigint not null,
  text                      varchar(255),
  duration                  integer,
  SCHOOL                    bigint,
  constraint pk_question primary key (question_id))
;

create table school (
  school_id                 bigint not null,
  name                      varchar(255),
  constraint pk_school primary key (school_id))
;

create table student (
  id                        bigint not null,
  name                      varchar(255),
  password                  varchar(255),
  email                     varchar(255),
  school_school_id          bigint,
  constraint pk_student primary key (id))
;

create table user (
  id                        bigint not null,
  name                      varchar(255),
  password                  varchar(255),
  email                     varchar(255),
  school_school_id          bigint,
  constraint pk_user primary key (id))
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

create sequence student_seq;

create sequence user_seq;

create sequence video_seq;

alter table question add constraint fk_question_school_1 foreign key (SCHOOL) references school (school_id) on delete restrict on update restrict;
create index ix_question_school_1 on question (SCHOOL);
alter table student add constraint fk_student_school_2 foreign key (school_school_id) references school (school_id) on delete restrict on update restrict;
create index ix_student_school_2 on student (school_school_id);
alter table user add constraint fk_user_school_3 foreign key (school_school_id) references school (school_id) on delete restrict on update restrict;
create index ix_user_school_3 on user (school_school_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists question;

drop table if exists school;

drop table if exists student;

drop table if exists user;

drop table if exists video;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists question_seq;

drop sequence if exists school_seq;

drop sequence if exists student_seq;

drop sequence if exists user_seq;

drop sequence if exists video_seq;

