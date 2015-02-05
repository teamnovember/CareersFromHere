# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table question (
  question_id               bigint auto_increment not null,
  text                      varchar(255),
  duration                  integer,
  SCHOOL                    bigint,
  constraint pk_question primary key (question_id))
;

create table school (
  school_id                 bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_school primary key (school_id))
;

create table student (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  password                  varchar(255),
  email                     varchar(255),
  school_school_id          bigint,
  constraint pk_student primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  password                  varchar(255),
  email                     varchar(255),
  school_school_id          bigint,
  constraint pk_user primary key (id))
;

create table video (
  id                        bigint auto_increment not null,
  title                     varchar(255),
  description               varchar(255),
  thumbnail_path            varchar(255),
  duration                  integer,
  approved                  tinyint(1) default 0,
  constraint pk_video primary key (id))
;

alter table question add constraint fk_question_school_1 foreign key (SCHOOL) references school (school_id) on delete restrict on update restrict;
create index ix_question_school_1 on question (SCHOOL);
alter table student add constraint fk_student_school_2 foreign key (school_school_id) references school (school_id) on delete restrict on update restrict;
create index ix_student_school_2 on student (school_school_id);
alter table user add constraint fk_user_school_3 foreign key (school_school_id) references school (school_id) on delete restrict on update restrict;
create index ix_user_school_3 on user (school_school_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table question;

drop table school;

drop table student;

drop table user;

drop table video;

SET FOREIGN_KEY_CHECKS=1;

