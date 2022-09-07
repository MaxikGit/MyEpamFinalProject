USE restaurantDB;

-- fulfill all tables --------------------

INSERT INTO role
VALUES (1, 'manager'),
       (2, 'client');

-- category table -----------------------------------

INSERT INTO category
VALUES (DEFAULT, 'Салат'),
       (DEFAULT, 'Meat & Fish');
INSERT INTO category
VALUES (DEFAULT, 'BBQ Food'),
       (DEFAULT, 'Drinks');
-- INSERT INTO category
-- VALUES (DEFAULT, 'Піцца'), (DEFAULT, 'Alcohol'), (DEFAULT, 'Deserts'),
--  (DEFAULT, 'Sushi'), (DEFAULT, 'Snacks');

-- status table -----------------------------------

INSERT INTO status
VALUES (1, 'receiving', DEFAULT),
       (2, 'cooking', DEFAULT),
       (3, 'delivery & payment', DEFAULT),
       (4, 'completed', DEFAULT);

-- dish table -----------------------------------

SET @salad = (SELECT id
              FROM category
              WHERE name = 'Салат');
INSERT INTO dish
VALUES (DEFAULT, 'Шуба', 70.30, DEFAULT, @salad, 'salad_fur_coat.webp'),
       (DEFAULT, 'Bulgarian', 50.20, DEFAULT, @salad, 'bulgarian.webp'),
       (DEFAULT, 'Grilled chicken with charred pineapple salad', 20.50, DEFAULT, @salad, 'grilled_chicken_with_charred_pineapple_salad.webp');
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
VALUES (DEFAULT, 'козюлько@gmail.com', 'Козел', 'Козюлько', 'Козюлько', DEFAULT, 2),
       (DEFAULT, 'бозюлько@gmail.com', 'Бозел', 'Бозюлько', 'Бозюлько', DEFAULT, 2),
       (DEFAULT, 'жозюлько@gmail.com', 'Жозел', 'Жозюлько', 'Жозюлько', DEFAULT, 2);
INSERT INTO user
VALUES (DEFAULT, 'Гозюлько@gmail.com', 'Гозел', 'Гозюлько', 'Гозюлько', DEFAULT, 2),
       (DEFAULT, 'vasylko@gmail.com', 'Vasya', 'Vasylko', 'Vasylko', DEFAULT, 2),
       (DEFAULT, 'murzilko@gmail.com', 'Murza', 'Murzilko', 'Murzilko', DEFAULT, 2);

-- custom table -----------------------------------
SET @client = (SELECT id
              FROM user
              WHERE role_id = 2 LIMIT 1);
INSERT INTO custom
VALUES (DEFAULT, DEFAULT, DEFAULT, @client, 1);

-- custom_has_dish table -----------------------------------
INSERT INTO custom_has_dish
VALUES (1, 1, 1, (SELECT price FROM dish WHERE id = 1)),
       (1, 4, 1, (SELECT price FROM dish WHERE id = 4));