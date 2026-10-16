-- 投票功能建表脚本（MySQL）
-- 执行一次即可

CREATE TABLE IF NOT EXISTS `vote` (
  `id`               BIGINT       NOT NULL AUTO_INCREMENT,
  `title`            VARCHAR(100) NOT NULL COMMENT '投票标题',
  `description`      TEXT                  COMMENT '投票事件简介',
  `cover_image_path` VARCHAR(500)          COMMENT '封面图',
  `multi_select`     TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否多选 0单选 1多选',
  `deadline`         DATETIME     NOT NULL COMMENT '统计截止时间',
  `voter_count`      INT          NOT NULL DEFAULT 0 COMMENT '参与投票人数',
  `view_count`       INT          NOT NULL DEFAULT 0 COMMENT '浏览数',
  `comments_count`   INT          NOT NULL DEFAULT 0 COMMENT '评论数',
  `is_top`           TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否置顶',
  `status`           TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '展示状态',
  `create_time`      DATETIME              COMMENT '创建时间',
  `create_name`      VARCHAR(50)           COMMENT '创建人',
  `update_time`      DATETIME              COMMENT '修改时间',
  `update_name`      VARCHAR(50)           COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `idx_status_top_time` (`status`, `is_top`, `create_time`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '投票';

CREATE TABLE IF NOT EXISTS `vote_option` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT,
  `vote_id`     BIGINT       NOT NULL COMMENT '投票id',
  `content`     VARCHAR(100) NOT NULL COMMENT '选项内容',
  `image_path`  VARCHAR(500)          COMMENT '选项图片',
  `sort`        INT          NOT NULL DEFAULT 0 COMMENT '排序 越小越靠前',
  `vote_count`  INT          NOT NULL DEFAULT 0 COMMENT '得票数',
  `create_time` DATETIME              COMMENT '创建时间',
  `create_name` VARCHAR(50)           COMMENT '创建人',
  PRIMARY KEY (`id`),
  KEY `idx_vote_id` (`vote_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '投票选项';

CREATE TABLE IF NOT EXISTS `vote_record` (
  `id`          BIGINT      NOT NULL AUTO_INCREMENT,
  `vote_id`     BIGINT      NOT NULL COMMENT '投票id',
  `option_id`   BIGINT      NOT NULL COMMENT '选项id',
  `player_id`   BIGINT      NOT NULL COMMENT '投票用户id',
  `create_time` DATETIME             COMMENT '投票时间',
  `create_name` VARCHAR(50)          COMMENT '投票用户名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_vote_option_player` (`vote_id`, `option_id`, `player_id`),
  KEY `idx_vote_player` (`vote_id`, `player_id`),
  KEY `idx_option_id` (`option_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '投票记录';
