# --- !Ups

alter table user add type integer;


# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

alter table video drop type;

SET FOREIGN_KEY_CHECKS=1;