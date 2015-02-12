# --- !Ups

create table category (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_category primary key (id))
;

create table category_video (
  id                        bigint auto_increment not null,
  category_id               bigint,
  video_id                  bigint,
  constraint pk_category_video primary key (id))
;

alter table category_video add constraint fk_category_video_video_7 foreign key (video_id) references video (id) on delete restrict on update restrict;
create index ix_category_video_video_7 on category_video (video_id);
alter table category_video add constraint fk_category_video_category_8 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_category_video_category_8 on category_video (category_id);

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table category;
drop table category_video;

SET FOREIGN_KEY_CHECKS=1;

