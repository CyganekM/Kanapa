CREATE
    DATABASE IF NOT EXISTS `kanapa`;

GO

USE
    `kanapa`;

GO

CREATE TABLE `roles`
(
    `id`        bigint NOT NULL AUTO_INCREMENT,
    `authority` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

GO

CREATE TABLE `users`
(
    `id`                bigint       NOT NULL AUTO_INCREMENT,
    `login`             varchar(45)  NOT NULL,
    `password`          varchar(250) NOT NULL,
    `name`              varchar(45)  NOT NULL,
    `surname`           varchar(45)  NOT NULL,
    `birthdate`         timestamp    NULL     DEFAULT NULL,
    `email`             varchar(45)  NOT NULL,
    `phone`             varchar(45)           DEFAULT NULL,
    `enabled`           tinyint(1)   NOT NULL DEFAULT '1',
    `rating`            decimal(3, 2)         DEFAULT NULL,
    `kanapic`           int          NOT NULL DEFAULT '0',
    `date_registration` timestamp    NOT NULL,
    PRIMARY KEY (`id`, `login`),
    UNIQUE KEY `login_UNIQUE` (`login`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 53
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

GO

CREATE TABLE `user_roles`
(
    `id`      bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `role_id` bigint NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_userroles_roles_idx` (`role_id`),
    KEY `fk_userroles_users` (`user_id`),
    CONSTRAINT `fk_userroles_roles` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_userroles_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

GO

CREATE TABLE `categories`
(
    `id`          bigint      NOT NULL AUTO_INCREMENT,
    `parent`      bigint       DEFAULT NULL,
    `name`        varchar(45) NOT NULL,
    `description` varchar(200) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_categories_idx` (`parent`),
    CONSTRAINT `fk_categories` FOREIGN KEY (`parent`) REFERENCES `categories` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 18
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

GO

CREATE TABLE `advertisements`
(
    `id`            bigint                                                  NOT NULL AUTO_INCREMENT,
    `users_id`      bigint                                                  NOT NULL,
    `categories_id` bigint                                                  NOT NULL,
    `name`          varchar(200) CHARACTER SET big5 COLLATE big5_chinese_ci NOT NULL,
    `description`   text,
    `price`         decimal(10, 2)                                               DEFAULT NULL,
    `date_start`    timestamp                                               NOT NULL,
    `date_close`    timestamp                                               NULL DEFAULT NULL,
    `date_bonus`    timestamp                                               NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_advertisement_user_idx` (`users_id`),
    KEY `fk_advertisement_category_idx` (`categories_id`),
    CONSTRAINT `fk_advertisement_category` FOREIGN KEY (`categories_id`) REFERENCES `categories` (`id`),
    CONSTRAINT `fk_advertisement_user` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 24
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

GO

CREATE TABLE `comments`
(
    `id`                bigint    NOT NULL AUTO_INCREMENT,
    `advertisements_id` bigint    NOT NULL,
    `users_id`          bigint    NOT NULL,
    `date`              timestamp NOT NULL,
    `message`           text      NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_coments_advertisements_idx` (`advertisements_id`),
    KEY `fk_coments_users_idx` (`users_id`),
    CONSTRAINT `fk_coments_advertisements` FOREIGN KEY (`advertisements_id`) REFERENCES `advertisements` (`id`) ON UPDATE RESTRICT,
    CONSTRAINT `fk_comments_users` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 26
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

GO

CREATE TABLE `kanapic_transactions`
(
    `id`                bigint    NOT NULL AUTO_INCREMENT,
    `users_id`          bigint    NOT NULL,
    `advertisements_id` bigint         DEFAULT NULL,
    `debit`             int            DEFAULT NULL,
    `credit`            int            DEFAULT NULL,
    `date`              timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_bonus_transactions_users_idx` (`users_id`),
    KEY `fk_bonus_transacton_advertisements_idx` (`advertisements_id`),
    CONSTRAINT `fk_kanapic_advertisements` FOREIGN KEY (`advertisements_id`) REFERENCES `advertisements` (`id`),
    CONSTRAINT `fk_kanapic_users` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

GO

CREATE TABLE `messages`
(
    `id`           bigint    NOT NULL AUTO_INCREMENT,
    `sender_id`    bigint         DEFAULT NULL,
    `recipient_id` bigint         DEFAULT NULL,
    `date`         timestamp NULL DEFAULT NULL,
    `message`      text,
    PRIMARY KEY (`id`),
    KEY `fk_messages_sender_users_idx` (`sender_id`),
    KEY `fk_messages_recipient_users_idx` (`recipient_id`),
    CONSTRAINT `fk_messages_recipient_users` FOREIGN KEY (`recipient_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_messages_sender_users` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

GO

CREATE TABLE `transactions`
(
    `id`                bigint    NOT NULL AUTO_INCREMENT,
    `advertisements_id` bigint    NOT NULL,
    `users_id`          bigint    NOT NULL,
    `date`              timestamp NOT NULL,
    `customer_flag`     tinyint(1) DEFAULT NULL,
    `rating`            tinyint    DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_transactions_advertisements_idx` (`advertisements_id`),
    KEY `fk_transactions_users_idx` (`users_id`),
    CONSTRAINT `fk_transactions_advertisements` FOREIGN KEY (`advertisements_id`) REFERENCES `advertisements` (`id`),
    CONSTRAINT `fk_transactions_users` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

GO

CREATE TABLE `token_black_list`
(
    `id`    bigint       NOT NULL AUTO_INCREMENT,
    `token` varchar(500) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `token_idx` (`token`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

GO

INSERT INTO `categories`
VALUES (1, NULL, 'Auto and transport', NULL),
       (2, NULL, 'Appliances', NULL),
       (3, NULL, 'Computer technology', NULL),
       (4, NULL, 'Phones and tablets', NULL),
       (5, NULL, 'Electronics', NULL),
       (6, NULL, 'Animals', NULL),
       (7, 3, 'Laptop', NULL),
       (8, 3, 'Computers', NULL),
       (9, NULL, 'Everything for kids and moms', NULL),
       (10, 9, 'Clothes for girls', NULL),
       (11, 9, 'Clothing for boys', NULL),
       (12, 9, 'Maternity clothes', NULL),
       (14, 1, 'Moto', 'Everything that rides on two wheels');

GO

INSERT INTO `roles`
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');

GO

INSERT INTO `users`
VALUES (1, 'admin', '$2a$10$zPhXrZ4aOoXNk7n101f8k.ATEv5ZqlZKQSI5Lj7F7FAO/Ltdzme7S', 'Maximus', 'Prime',
        '1988-11-23 21:00:00', 'cyganek@tut.by', '+375(29)8230902', 1, 9.00, 25, '2022-05-17 05:24:07');

GO

INSERT INTO `user_roles`
VALUES (1, 1, 2);

GO

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

GO

INSERT INTO `advertisements`
VALUES (1, 1, 7, 'HP laptop', 'Very good laptop', 500.00, '2022-05-14 21:00:00', NULL, '2022-05-28 19:04:28'),
       (10, 1, 8, 'Lenovo Comp', 'So-so comp', 350.00, '2022-05-13 21:00:00', NULL, NULL),
       (11, 1, 1, 'VW passat', 'Round wheels, transparent glass', 7000.00, '2022-05-12 21:00:00', '2022-05-14 21:00:00',
        NULL),
       (12, 2, 4, 'Apple phone', 'The condition is good. You can crack nuts with them', 50.00, '2022-05-12 21:00:00',
        '2022-05-30 19:04:28', NULL),
       (13, 22, 6, 'Dog', 'big boy', 1200.00, '2022-05-18 10:45:28', NULL, NULL),
       (14, 21, 10, 'Jacket', 'Season for spring or autumn. height 98-104', 80.00, '2022-05-24 07:30:28', NULL, NULL),
       (15, 20, 10, 'Dress', 'A very stylish dress for a little princess', 50.00, '2022-05-25 05:28', NULL, NULL),
       (16, 21, 10, 'Denim pants', 'Pink. 116-122', 20.00, '2022-05-26 05:42:00', NULL, NULL),
       (17, 16, 11, 'Denim pants', 'Blue. ', 21.00, '2022-05-26 07:42:00', NULL, NULL),
       (18, 15, 11, 'T-shirt', 'white with a pattern', 10.00, '2022-05-27 07:47:00', NULL, NULL),
       (19, 2, 11, 'School shirt', 'school shirt', 5.00, '2022-05-27 08:00:00', NULL, NULL),
       (20, 2, 6, 'Cat-Dog', 'sex - ?', 12000.00, '2022-06-13 06:02:08', '2022-06-13 06:48:53', NULL);

GO

INSERT INTO `comments`
VALUES (16, 13, 1, '2022-05-20 16:40:58', 'Are you changing it for a brick?'),
       (23, 10, 1, '2022-06-12 02:49:51', 'Are you changing it for a brick?');

GO

INSERT INTO `kanapic_transactions`
VALUES (1, 1, NULL, 20, NULL, '2022-05-24 17:47:34'),
       (2, 2, 12, NULL, 3, '2022-05-24 19:04:28'),
       (3, 2, NULL, 30, NULL, '2022-06-12 05:55:29'),
       (4, 2, 12, NULL, 3, '2022-06-13 05:35:09');

GO

INSERT INTO `messages`
VALUES (1, 1, 2, '2022-05-14 18:47:00', 'Hello. how are you?'),
       (2, 2, 1, '2022-05-14 18:48:00', 'Hi. So-so'),
       (3, 1, 2, '2022-05-21 08:27:13', 'Is it possible to exchange for a brick?'),
       (4, 1, 2, '2022-05-21 08:39:45', 'Hi! How are you?');

GO

INSERT INTO `transactions`
VALUES (1, 12, 1, '2022-05-14 21:00:00', 1, NULL),
       (2, 1, 2, '2022-05-14 21:00:00', 1, NULL),
       (3, 10, 15, '2022-05-23 08:54:03', 1, 10),
       (5, 11, 15, '2022-05-23 10:55:24', 1, 7),
       (6, 10, 2, '2022-05-23 10:57:13', 1, 10),
       (9, 13, 1, '2022-06-10 08:26:42', 0, NULL);

GO

INSERT INTO `user_roles`
VALUES (2, 2, 2),
       (3, 2, 1),
       (4, 16, 1),
       (5, 15, 1),
       (6, 20, 1),
       (7, 22, 1);