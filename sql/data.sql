insert into Dish (id, name, dish_type)
values (1, 'Salade fraîche', 'START'),
       (2, 'Poulet grillé', 'MAIN'),
       (3, 'Riz aux légumes', 'MAIN'),
       (4, 'Gâteau au chocolat', 'DESSERT'),
       (5, 'Salade de fruits', 'DESSERT');

insert into Ingredient (id, name, price, category)
values (1, 'Laitue', 800.00, 'VEGETABLE'),
       (2, 'Tomate', 600.00, 'VEGETABLE'),
       (3, 'Poulet', 4500.00, 'ANIMAL'),
       (4, 'Chocolat', 3000.00, 'OTHER'),
       (5, 'Beurre', 2500.00, 'DAIRY');

select setval('dish_id_seq', (select max(id) from Dish));
select setval('ingredient_id_seq', (select max(id) from Ingredient));

insert into dish_ingredient (id, id_dish, id_ingredient, quantity_required, unit)
values (1, 1, 1, 0.20, 'KG'),
       (2, 1, 2, 0.15, 'KG'),
       (3, 2, 3, 1.00, 'KG'),
       (4, 4, 4, 0.30, 'KG'),
       (5, 4, 5, 0.20, 'KG');


select setval('dish_ingredient_id_seq', (select max(id) from dish_ingredient));

update dish
set selling_price = 3500.00
where id = 1;
update dish
set selling_price = 12000.00
where id = 2;
update dish
set selling_price = 8000.00
where id = 4;

insert into stock_movement (id_ingredient, quantity, unit, creation_datetime, type)
values (1, 5.0, 'KG', '2024-01-05 08:00', 'IN'),
       (1, 0.2, 'KG', '2024-01-06 12:00', 'OUT'),
       (2, 4.0, 'KG', '2024-01-05 08:00', 'IN'),
       (2, 0.15, 'KG', '2024-01-06 12:00', 'OUT'),
       (3, 10.0, 'KG', '2024-01-04 09:00', 'IN'),
       (3, 1.0, 'KG', '2024-01-06 13:00', 'OUT'),
       (4, 3.0, 'KG', '2024-01-05 10:00', 'IN'),
       (4, 0.3, 'KG', '2024-01-06 14:00', 'OUT'),
       (5, 2.5, 'KG', '2024-01-05 10:00', 'IN'),
       (5, 0.2, 'KG', '2024-01-06 14:00', 'OUT');

select setval('stock_movement_id_seq', (select max(id) from stock_movement));