create sequence hibernate_sequence start 1 increment 1;
create table car
(
    id           int8 not null,
    uuid         varchar(255),
    version      int8 not null,
    created_at   timestamp,
    engine       float8,
    fuel         varchar(255),
    image_id     int8,
    manufacturer varchar(255),
    model        varchar(255),
    year         int4,
    primary key (id)
);
create table car_item
(
    id           int8 not null,
    uuid         varchar(255),
    version      int8 not null,
    available    int8,
    category     varchar(255),
    created_at   timestamp,
    image_id     int8,
    price        numeric(19, 2),
    product_code varchar(255),
    sub_category varchar(255),
    primary key (id)
);
create table car_item_cars
(
    car_items_id int8 not null,
    cars_id      int8 not null,
    primary key (car_items_id, cars_id)
);
create table order_item
(
    id          int8 not null,
    uuid        varchar(255),
    version     int8 not null,
    quantity    int4 not null,
    car_item_id int8,
    order_id    int8,
    primary key (id)
);
create table orders
(
    id           int8 not null,
    uuid         varchar(255),
    version      int8 not null,
    created_at   timestamp,
    delivery     varchar(255),
    status       varchar(255),
    updated_at   timestamp,
    recipient_id int8,
    primary key (id)
);
create table recipient
(
    id        int8 not null,
    uuid      varchar(255),
    version   int8 not null,
    city      varchar(255),
    email     varchar(255),
    firstname varchar(255),
    lastname  varchar(255),
    phone     varchar(255),
    street    varchar(255),
    zip_code  varchar(255),
    primary key (id)
);
create table upload
(
    id           int8 not null,
    uuid         varchar(255),
    version      int8 not null,
    content_type varchar(255),
    created_at   timestamp,
    file         bytea,
    file_name    varchar(255),
    primary key (id)
);
create table users
(
    id         int8 not null,
    uuid       varchar(255),
    version    int8 not null,
    created_at timestamp,
    password   varchar(255),
    updated_at timestamp,
    username   varchar(255),
    primary key (id)
);
create table users_roles
(
    user_id int8 not null,
    role    varchar(255)
);
alter table car_item
    add constraint UK_1nqmu8q2yo2ual4dvarlo8voo unique (product_code);
alter table car_item_cars
    add constraint FKeviwbmt5njqts65yw6uw97ger foreign key (cars_id) references car;
alter table car_item_cars
    add constraint FK1c8xrn531q56sm60lnj9xtlps foreign key (car_items_id) references car_item;
alter table order_item
    add constraint FK7ekxf049hkncsiw1k0nbqtoj2 foreign key (car_item_id) references car_item;
alter table order_item
    add constraint FKt4dc2r9nbvbujrljv3e23iibt foreign key (order_id) references orders;
alter table orders
    add constraint FKcxwo1jbmo15jih4b5qjclvye8 foreign key (recipient_id) references recipient;
alter table users_roles
    add constraint FK2o0jvgh89lemvvo17cbqvdxaa foreign key (user_id) references users;
