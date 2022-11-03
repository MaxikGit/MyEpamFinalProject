USE restaurantDB;

-- fulfill all tables --------------------

INSERT INTO role
VALUES (1, 'manager'),
       (2, 'client');

-- category table -----------------------------------

INSERT INTO category
VALUES (DEFAULT, 'category.salad'),
       (DEFAULT, 'category.meatfish');
INSERT INTO category
VALUES (DEFAULT, 'category.bbq'),
       (DEFAULT, 'category.drinks');
-- INSERT INTO category
-- VALUES (DEFAULT, 'Піцца'), (DEFAULT, 'Alcohol'), (DEFAULT, 'Deserts'),
--  (DEFAULT, 'Sushi'), (DEFAULT, 'Snacks');

-- status table -----------------------------------

INSERT INTO status
VALUES (1, 'order.status1', 'receiving'),
       (2, 'order.status2', 'cooking'),
       (3, 'order.status3', 'delivery & payment'),
       (4, 'order.status4', 'completed');

-- dish table -----------------------------------

SET @salad = (SELECT id
              FROM category
              WHERE name = 'Салат');
INSERT INTO dish
VALUES (DEFAULT, 'Шуба', 70.30, DEFAULT, @salad, 'salad_fur_coat.webp'),
       (DEFAULT, 'Bulgarian', 50.20, DEFAULT, @salad, 'bulgarian.webp'),
       (DEFAULT, 'Grilled chicken with charred pineapple salad', 20.50, DEFAULT, @salad, 'grilled_chicken_with_charred_pineapple_salad.webp'),
       (DEFAULT, 'Best-ever Brussels sprouts', 55.50, DEFAULT, @salad, 'brussels_sprouts.webp');
SET @meat = (SELECT id
             FROM category
             WHERE name = 'Meat & Fish');
INSERT INTO dish
VALUES (DEFAULT, 'Печеня', 170.50, DEFAULT, @meat, 'zharkoe.webp'),
       (DEFAULT, 'Fried tune', 50.50, DEFAULT, @meat, 'fried_tune_new.webp'),
       (DEFAULT, 'Perfect pork belly', 220.10, DEFAULT, @meat, 'perfect_pork_belly.webp'),
       (DEFAULT, 'Gunpowder lamb', 270.25, DEFAULT, @meat, 'gunpowder_lamb.webp');
SET @bbq = (SELECT id
            FROM category
            WHERE name = 'BBQ Food');
INSERT INTO dish
VALUES (DEFAULT, 'BBQ british ribs', 130.20, DEFAULT, @bbq, 'bbq_british_ribs.webp'),
       (DEFAULT, 'Шашлик з ошийка', 225.50, DEFAULT, @bbq, 'shashlyk_z_oshijka.webp'),
       (DEFAULT, 'Butterflied prawn skewers', 320.10, DEFAULT, @bbq, 'butterflied_prawn_skewers.webp'),
       (DEFAULT, 'Whole burnt aubergine', 70.25, DEFAULT, @bbq, 'whole_burnt_aubergine.webp');
SET @drink = (SELECT id
              FROM category
              WHERE name = 'Drinks');
INSERT INTO dish
VALUES (DEFAULT, 'Вода', 10.00, DEFAULT, @drink, 'bottle_of_water.webp'),
       (DEFAULT, 'Vodka Khortytsa', 55.50, DEFAULT, @drink, 'khortitsa.webp'),
       (DEFAULT, 'Coca-Cola', 20.10, DEFAULT, @drink, 'bottle_of_coca_cola.webp'),
       (DEFAULT, 'Whisky Jameson', 70.25, DEFAULT, @drink, 'jameson.webp');

-- users table -----------------------------------

INSERT INTO user
VALUES (DEFAULT, 'klimenko@gmail.com', 'Demon', 'Klimenko', 'Klimenko', DEFAULT, 1),
       (DEFAULT, 'kulpaka@gmail.com', 'Major', 'Kulpaka', 'Kulpaka', DEFAULT, 2),
       (DEFAULT, 'ivanov@gmail.com', 'Ivan', 'Ivanko', 'Ivanko', DEFAULT, 2),
       (DEFAULT, 'petrenko@gmail.com', 'Petro', 'Petrenko', 'Petrenko', DEFAULT, 2);
INSERT INTO user
VALUES (DEFAULT, 'kozulko@gmail.com', 'Ігнат', 'Козюлько', 'Kozulko', DEFAULT, 2),
       (DEFAULT, 'bozulko@gmail.com', 'Богдан', 'Бозюлько', 'Bozulko', DEFAULT, 2),
       (DEFAULT, 'zhozulko@gmail.com', 'Жозеп', 'Жозюлько', 'Zhozulko', DEFAULT, 2);
INSERT INTO user
VALUES (DEFAULT, 'gozulko@gmail.com', 'Гозел', 'Гозюлько', 'Gozulko', DEFAULT, 2),
       (DEFAULT, 'vasylko@gmail.com', 'Vasya', 'Vasylko', 'Vasylko', DEFAULT, 2),
       (DEFAULT, 'murzilko@gmail.com', 'Murza', 'Murzilko', 'Murzilko', DEFAULT, 2),
       (DEFAULT, 'mohito@gmail.com',  'Оля', 'Мохіто', 'Mohito', DEFAULT, 2),
       (DEFAULT, 'gggyyy@gmail.com', 'Саша', 'fddsd', '1234', DEFAULT, 2),
       (DEFAULT, 'gghh@gmail.com', 'какашка', 'какашка', '5566', DEFAULT, 2),
       (DEFAULT, 'ho@ukr.net', 'Хо', 'А', 'hohoho', DEFAULT, 2);

-- custom table -----------------------------------
SET @client = (SELECT id
              FROM user
              WHERE role_id = 2 LIMIT 1);
INSERT INTO custom
VALUES (20, 831.10, '2022-09-19 17:16:54', 3, 4),
       (21, 1657.05, '2022-09-19 17:20:11', 9, 4),
       (22, 352.50, '2022-09-19 17:22:14', 1, 4),
       (23, 411.90, '2022-09-21 14:12:47', 11, 4),
       (25, 657.30, '2022-09-26 16:15:19', 1, 4),
       (26, 1262.50, '2022-09-26 16:53:56', 4, 2),
       (27, 191.60, '2022-09-26 18:13:52', 1, 4),
       (28, 2057.85, '2022-09-30 16:38:53', 1, 4),
       (29, 1431.70, '2022-10-03 08:29:48', 8, 3),
       (31, 463.35, '2022-10-07 12:28:56', 1, 4),
       (32, 417.70, '2022-10-07 12:36:23', 5, 2),
       (33, 801.45, '2022-10-07 12:40:57', 3, 4),
       (34, 581.20, '2022-10-07 12:53:34', 6, 3),
       (38, 1411.30, '2022-10-08 14:58:11', 2, 3),
       (39, 161.40, '2022-10-08 15:02:05', 2, 2),
       (40, 1179.45, '2022-10-10 13:06:47', 1, 3),
       (41, 346.30, '2022-10-15 10:19:22', 3, 3),
       (42, 151.20, '2022-10-16 15:37:33', 4, 2),
       (43, 287.50, '2022-10-18 08:34:22', 1, 2),
       (44, 811.65, '2022-10-18 09:34:20', 14, 2),
       (45, 290.70, '2022-10-23 13:39:08', 1, 4),
       (46, 702.10, '2022-10-24 09:40:08', 11, 4),
       (47, 651.20, '2022-10-24 16:14:04', 11, 1);

-- custom_has_dish table -----------------------------------
INSERT INTO custom_has_dish
VALUES (20, 5, 1, 50.50),
       (20, 6, 1, 220.10),
       (20, 7, 2, 270.25),
       (20, 12, 2, 10.00),
       (21, 4, 1, 170.50),
       (21, 5, 2, 50.50),
       (21, 6, 2, 220.10),
       (21, 10, 1, 320.10),
       (21, 11, 1, 70.25),
       (21, 13, 10, 55.50),
       (22, 1, 1, 70.30),
       (22, 2, 1, 50.20),
       (22, 3, 3, 20.50),
       (22, 4, 1, 170.50),
       (23, 1, 1, 70.30),
       (23, 2, 3, 50.20),
       (23, 3, 1, 20.50),
       (23, 4, 1, 170.50),
       (25, 2, 1, 50.20),
       (25, 3, 1, 20.50),
       (25, 4, 1, 170.50),
       (25, 7, 1, 270.25),
       (25, 13, 1, 55.50),
       (25, 14, 1, 20.10),
       (25, 15, 1, 70.25),
       (26, 2, 2, 50.20),
       (26, 3, 1, 20.50),
       (26, 7, 2, 270.25),
       (26, 10, 1, 320.10),
       (26, 11, 1, 70.25),
       (26, 15, 3, 70.25),
       (27, 2, 3, 50.20),
       (27, 3, 2, 20.50),
       (27, 10, 1, 320.10),
       (27, 12, 3, 10.00),
       (27, 13, 2, 55.50),
       (27, 15, 3, 70.25),
       (28, 1, 3, 70.30),
       (28, 2, 3, 50.20),
       (28, 3, 4, 20.50),
       (28, 4, 6, 170.50),
       (28, 5, 2, 50.50),
       (28, 6, 1, 220.10),
       (28, 7, 1, 270.25),
       (28, 8, 3, 130.20),
       (28, 10, 4, 320.10),
       (28, 11, 1, 70.25),
       (28, 12, 1, 10.00),
       (29, 1, 3, 70.30),
       (29, 2, 4, 50.20),
       (29, 3, 2, 20.50),
       (29, 4, 2, 170.50),
       (29, 8, 1, 130.20),
       (29, 11, 4, 70.25),
       (29, 13, 3, 55.50),
       (29, 14, 3, 20.10),
       (31, 1, 1, 70.30),
       (31, 2, 1, 50.20),
       (31, 3, 3, 20.50),
       (31, 5, 1, 50.50),
       (31, 14, 1, 20.10),
       (31, 15, 3, 70.25),
       (32, 2, 1, 50.20),
       (32, 3, 1, 20.50),
       (32, 4, 1, 170.50),
       (32, 12, 1, 10.00),
       (32, 13, 3, 55.50),
       (33, 2, 1, 50.20),
       (33, 7, 2, 270.25),
       (33, 15, 3, 70.25),
       (34, 8, 1, 130.20),
       (34, 9, 2, 225.50),
       (38, 2, 3, 50.20),
       (38, 3, 1, 20.50),
       (38, 9, 2, 225.50),
       (38, 10, 3, 320.10),
       (38, 12, 1, 10.00),
       (38, 13, 4, 55.50),
       (38, 14, 1, 20.10),
       (38, 15, 1, 70.25),
       (39, 1, 1, 70.30),
       (39, 2, 2, 50.20),
       (39, 3, 2, 20.50),
       (39, 4, 1, 170.50),
       (39, 12, 2, 10.00),
       (40, 1, 2, 70.30),
       (40, 2, 4, 50.20),
       (40, 3, 2, 20.50),
       (40, 4, 3, 170.50),
       (40, 5, 1, 50.50),
       (40, 6, 1, 220.10),
       (40, 13, 3, 55.50),
       (40, 14, 1, 20.10),
       (40, 15, 3, 70.25),
       (41, 2, 1, 50.20),
       (41, 3, 1, 20.50),
       (41, 6, 1, 220.10),
       (41, 13, 1, 55.50),
       (42, 2, 1, 50.20),
       (42, 5, 2, 50.50),
       (42, 13, 3, 55.50),
       (42, 15, 3, 70.25),
       (43, 2, 1, 50.20),
       (43, 3, 1, 20.50),
       (43, 5, 2, 50.50),
       (43, 8, 1, 130.20),
       (43, 9, 1, 225.50),
       (43, 10, 1, 320.10),
       (43, 13, 1, 55.50),
       (43, 14, 3, 20.10),
       (44, 8, 2, 130.20),
       (44, 9, 2, 225.50),
       (44, 11, 1, 70.25),
       (44, 12, 3, 10.00),
       (45, 5, 1, 50.50),
       (45, 6, 1, 220.10),
       (45, 14, 1, 20.10),
       (46, 2, 2, 50.20),
       (46, 3, 1, 20.50),
       (46, 8, 1, 130.20),
       (46, 9, 2, 225.50),
       (47, 2, 1, 50.20),
       (47, 3, 1, 20.50),
       (47, 8, 2, 130.20),
       (47, 10, 1, 320.10);