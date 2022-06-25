USE
`kanapa`;

INSERT INTO `users`
VALUES (2, 'bob', '$2a$10$zPhXrZ4aOoXNk7n101f8k.ATEv5ZqlZKQSI5Lj7F7FAO/Ltdzme7S', 'Bob', 'Sponge',
        '1981-08-07 21:00:00', 'sbob@gmail.com', '911', 1, NULL, 29, '2022-05-17 05:24:07'),
       (15, 'homer', '11112', 'Homer', 'Simpson', '1984-08-03 21:00:00', 'simpson@tut.by', '+375(29)8464946', 1, NULL,
        5, '2022-05-17 05:24:07'),
       (16, 'kenny', '12345', 'Kenny', 'McCormick', '1984-08-03 21:00:00', 'McCormick@tut.by', '+375(29)8464941', 1,
        8.00, 5, '2022-05-17 05:24:40'),
       (20, 'harry', '12345', 'Harry', 'Potter', '1984-08-03 21:00:00', 'cyganek@tut.by', '+375(29)8250902', 1, 9.70,
        10, '2022-05-23 06:19:06'),
       (21, 'nils', '12345', 'Karlsson', 'Nils', '1984-08-03 21:00:00', 'Karlsson@gmail.com', '+375(33)2356984', 1,
        5.80, 5, '2022-05-24 06:19:06'),
       (22, 'user', '$2a$10$zPhXrZ4aOoXNk7n101f8k.ATEv5ZqlZKQSI5Lj7F7FAO/Ltdzme7S', 'Maxim', 'Cyganek',
        '1990-05-24 21:00:00', 'cyganek@tut.by', '+375(29)8250902', 1, NULL, 2030, '2022-06-07 08:27:40');

INSERT INTO `advertisements`
VALUES (1, 1, 7, 'HP laptop', 'Very good laptop', 500.00, NULL, '2022-05-14 21:00:00', NULL, '2022-05-28 19:04:28'),
       (10, 1, 8, 'Lenovo Comp', 'So-so comp', 350.00, NULL, '2022-05-13 21:00:00', NULL, NULL),
       (11, 1, 1, 'VW passat', 'Round wheels, transparent glass', 7000.00, NULL, '2022-05-12 21:00:00',
        '2022-05-14 21:00:00', NULL),
       (12, 2, 4, 'Apple phone', 'The condition is good. You can crack nuts with them', 50.00, NULL,
        '2022-05-12 21:00:00', NULL, '2022-05-30 19:04:28'),
       (13, 22, 6, 'Dog', 'big boy', 1200.00, NULL, '2022-05-18 10:45:28', NULL, NULL),
       (14, 21, 10, 'Jacket', 'Season for spring or autumn. height 98-104', 80.00, NULL, '2022-05-24 07:30:28', NULL,
        NULL),
       (15, 20, 10, 'Dress', 'A very stylish dress for a little princess', 50.00, NULL, '2022-05-25 05:30:28', NULL,
        NULL),
       (16, 21, 10, 'Denim pants', 'Pink. 116-122', 20.00, NULL, '2022-05-26 05:42:00', NULL, NULL),
       (17, 16, 11, 'Denim pants', 'Blue. ', 21.00, NULL, '2022-05-26 07:42:00', NULL, NULL),
       (18, 15, 11, 'T-shirt', 'white with a pattern', 10.00, NULL, '2022-05-27 07:47:00', NULL, NULL),
       (19, 2, 11, 'School shirt', 'school shirt', 5.00, NULL, '2022-05-27 08:00:00', NULL, NULL),
       (20, 2, 6, 'Cat-Dog', 'sex - ?', 12000.00, NULL, '2022-06-13 06:02:08', '2022-06-13 06:48:53', NULL);

INSERT INTO `comments`
VALUES (16, 13, 1, '2022-05-20 16:40:58', 'Are you changing it for a brick?'),
       (23, 10, 1, '2022-06-12 02:49:51', 'Are you changing it for a brick?');

INSERT INTO `kanapic_transactions`
VALUES (1, 1, NULL, 20, NULL, '2022-05-24 17:47:34'),
       (2, 2, 12, NULL, 3, '2022-05-24 19:04:28'),
       (3, 2, NULL, 30, NULL, '2022-06-12 05:55:29'),
       (4, 2, 12, NULL, 3, '2022-06-13 05:35:09');

INSERT INTO `messages`
VALUES (1, 1, 2, '2022-05-14 18:47:00', 'Hello. how are you?'),
       (2, 2, 1, '2022-05-14 18:48:00', 'Hi. So-so'),
       (3, 1, 2, '2022-05-21 08:27:13', 'Is it possible to exchange for a brick?'),
       (4, 1, 2, '2022-05-21 08:39:45', 'Hi! How are you?');

INSERT INTO `transactions`
VALUES (1, 12, 1, '2022-05-14 21:00:00', 1, NULL),
       (2, 1, 2, '2022-05-14 21:00:00', 1, NULL),
       (3, 10, 15, '2022-05-23 08:54:03', 1, 10),
       (5, 11, 15, '2022-05-23 10:55:24', 1, 7),
       (6, 10, 2, '2022-05-23 10:57:13', 1, 10),
       (9, 13, 1, '2022-06-10 08:26:42', 0, NULL);

INSERT INTO `user_roles`
VALUES (2, 2, 2),
       (3, 2, 1),
       (4, 16, 1),
       (5, 15, 1),
       (6, 20, 1),
       (7, 22, 1);
