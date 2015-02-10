# --- !Ups

alter table user add discriminator varchar(255);


# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

alter table user drop discriminator;

SET FOREIGN_KEY_CHECKS=1;

