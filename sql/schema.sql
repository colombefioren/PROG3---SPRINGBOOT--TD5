create type unit_type as enum ('PCS','KG','L');
create type category as enum ('VEGETABLE','ANIMAL','MARINE','DAIRY','OTHER');
create type dish_type as enum ('START','MAIN','DESSERT');
create type movement_type as enum ('OUT','IN');

create table Dish
(
    id        serial primary key,
    name      varchar(255) not null,
    dish_type dish_type    not null,
    selling_price numeric(10, 2)
);

create table Ingredient
(
    id       serial primary key,
    name     varchar(255)   not null,
    price    numeric(10, 2) not null,
    category category       not null
);

create table dish_ingredient
(
    id                serial primary key,
    id_dish           int            not null,
    id_ingredient     int            not null,
    quantity_required numeric(10, 2) not null,
    unit              unit_type      not null,
    constraint dish_fk
        foreign key (id_dish)
            references Dish (id)
            on delete cascade,
    constraint ingredient_fk
        foreign key (id_ingredient)
            references ingredient (id)
            on delete cascade,
    constraint dish_ingredient_unique
        unique (id_dish, id_ingredient)
);

create table stock_movement
(
    id                serial primary key,
    id_ingredient     int           not null,
    quantity          numeric(10, 2),
    type              movement_type not null,
    unit              unit_type     not null,
    creation_datetime timestamp     not null default current_timestamp,
    constraint ingredient_fk
        foreign key (id_ingredient)
            references ingredient (id)
            on delete cascade
);