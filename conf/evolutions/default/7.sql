# --- !Ups

alter table question add active tinyint(1) default 1;
alter table question add ordering bigint;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

alter table question drop active;
alter table question drop ordering;

SET FOREIGN_KEY_CHECKS=1;
