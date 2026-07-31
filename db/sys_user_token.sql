CREATE TABLE `sys_user_token` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `access_token` varchar(1024) DEFAULT NULL COMMENT '当前有效 access token',
  `access_expire_time` datetime DEFAULT NULL COMMENT 'access token 过期时间',
  `refresh_token` varchar(1024) DEFAULT NULL COMMENT '当前有效 refresh token',
  `refresh_expire_time` datetime DEFAULT NULL COMMENT 'refresh token 过期时间',
  `token_version` int DEFAULT 1 COMMENT 'token 版本号，用于强制失效',
  `status_flag` tinyint DEFAULT 1 COMMENT '状态，1-有效，0-失效',
  `update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户 token 会话表';
