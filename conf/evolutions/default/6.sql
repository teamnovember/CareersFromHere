# --- !Ups

alter table user add approved tinyint(1) default 0;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

alter table user drop approved;

SET FOREIGN_KEY_CHECKS=1;
