# --- !Ups

create table category (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_category primary key (id))
;

create table vidcat (
  id                        bigint auto_increment not null,
  category_id               bigint,
  video_id                  bigint,
  constraint pk_vidcat primary key (id))
;

alter table vidcat add constraint fk_vidcat_video_7 foreign key (video_id) references video (id) on delete restrict on update restrict;
create index ix_vidcat_video_7 on vidcat (video_id);
alter table vidcat add constraint fk_vidcat_category_8 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_vidcat_category_8 on vidcat (category_id);

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table category;
drop table vidcat;

SET FOREIGN_KEY_CHECKS=1;

