-- auto-generated definition
create table order_record
(
    id          serial
        constraint order_record_pk
            primary key,
    user_id     integer                             not null,
    product_id  integer                             not null,
    price       double precision                    not null,
    quantity    integer   default 1                 not null,
    payment     double precision,
    create_time timestamp default CURRENT_TIMESTAMP not null,
    description varchar(128)
);

comment on table order_record is '订单记录表';

comment on column order_record.user_id is '用户id';

comment on column order_record.product_id is '产品id';

comment on column order_record.price is '下单价格';

comment on column order_record.quantity is '下单数量';

comment on column order_record.payment is '实付价格';

comment on column order_record.create_time is '下单时间';


