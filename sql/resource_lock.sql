-- auto-generated definition
create table resource_lock
(
    id          serial
        constraint resource_lock_pk
            primary key,
    lock_name   varchar(128) not null,
    description varchar(128),
    create_time timestamp default CURRENT_TIMESTAMP
);

comment on table resource_lock is '悲观锁测试表';

comment on column resource_lock.lock_name is '锁定的名称';

comment on column resource_lock.description is '描述';

comment on column resource_lock.create_time is '上锁时间';

create unique index resource_lock_method_name_uindex
    on resource_lock (lock_name);

