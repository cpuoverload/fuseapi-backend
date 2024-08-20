create database if not exists fuseapi;

use fuseapi;

create table if not exists `user`
(
    `id`          bigint auto_increment comment 'id' primary key,
    `username`    varchar(256)                           not null comment '账号',
    `password`    varchar(256)                           not null comment '密码',
    `nickname`    varchar(256)                           null comment '昵称',
    `role`        varchar(256) default 'user'            not null comment '角色',
    `access_key`  varchar(512)                           not null comment 'accessKey',
    `secret_key`  varchar(512)                           not null comment 'secretKey',
    `create_time` datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `deleted`     tinyint      default 0                 not null comment '逻辑删除'
) comment '用户' collate = utf8mb4_unicode_ci;

create table if not exists `api_detail`
(
    `id`             bigint                             not null auto_increment primary key,
    `api_name`       varchar(256)                       not null comment '接口名称',
    `description`    varchar(256)                       null comment '描述',
    `method`         varchar(256)                       not null comment '请求方法',
    `api_path`       varchar(512)                       not null comment '接口路径',
    `request_header` json                               null comment '请求头（JSON）',
    `query_params`   json                               null comment '请求 query 参数（JSON）',
    `body_params`    json                               null comment '请求 body 参数（JSON）',
    `response_body`  json                               not null comment '响应体（JSON）',
    `response_error` json                               not null comment '响应错误（JSON）',
    `status`         int      default 0                 not null comment '接口状态（0-关闭，1-开启）',
    `userId`         bigint                             not null comment '创建者 id',
    `create_time`    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time`    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `deleted`        tinyint  default 0                 not null comment '逻辑删除'
) comment '接口详情';

create table if not exists `user_api_invocation`
(
    `id`              bigint                             not null auto_increment primary key,
    `user_id`         bigint                             not null comment '用户 id',
    `api_id`          bigint                             not null comment '接口 id',
    `remaining_calls` int      default 0                 not null comment '剩余调用次数',
    `used_calls`      int      default 0                 not null comment '已调用次数',
    `create_time`     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time`     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `deleted`         tinyint  default 0                 not null comment '逻辑删除'
) comment '用户调用接口关系';
