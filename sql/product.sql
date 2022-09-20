-- auto-generated definition
create table product
(
    id          serial
        constraint product_pk
            primary key,
    name        varchar(64),
    description varchar(255),
    price       double precision                    not null,
    stock       integer                             not null,
    create_time timestamp default CURRENT_TIMESTAMP not null,
    update_time timestamp default CURRENT_TIMESTAMP not null
);

comment on column product.name is '产品名称';

comment on column product.description is '产品描述';

comment on column product.price is '价格';

comment on column product.stock is '库存';