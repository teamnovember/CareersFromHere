# --- !Ups

alter table video_clip add audio_clip_path varchar(255);

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

alter table video_clip drop audio_clip_path;

SET FOREIGN_KEY_CHECKS=1;
