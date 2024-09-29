INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (name)
VALUES ('KFC'),
       ('McDonald’s');

INSERT INTO MENU_ITEM (name, price, restaurant_id)
VALUES ('Gamburger', 10050, 1),
       ('Poached eggs', 25000, 1),
       ('Marbled Beef Steak', 83500, 2);

INSERT INTO VOTE (user_id, restaurant_id)
VALUES (1, 2),
       (2, 1);
