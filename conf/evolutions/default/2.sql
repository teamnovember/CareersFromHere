# --- !Ups

alter table video drop duration;
alter table video modify description longtext;

create table video_clip (
  id                        bigint auto_increment not null,
  video_clip_path           varchar(255),
  duration                  integer,
  video_id                  bigint,
  question_question_id      bigint,
  constraint pk_video_clip primary key (id))
;

alter table video_clip add constraint fk_video_clip_video_4 foreign key (video_id) references video (id) on delete restrict on update restrict;
create index ix_video_clip_video_4 on video_clip (video_id);
alter table video_clip add constraint fk_video_clip_question_5 foreign key (question_question_id) references question (question_id) on delete restrict on update restrict;
create index ix_video_clip_question_5 on video_clip (question_question_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

alter table video add duration;
alter table video modify description varchar(255);

drop table video_clip;

SET FOREIGN_KEY_CHECKS=1;

