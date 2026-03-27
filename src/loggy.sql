CREATE TABLE `logs` (
  `uuid` char(40) COLLATE utf8_unicode_ci NOT NULL,
  `title` char(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `content` text COLLATE utf8_unicode_ci,
  `createTimestamp` date DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

LOCK TABLES `logs` WRITE;
INSERT INTO `logs` VALUES ('ac299eb1-599e-4599-b22b-95e889448793','Two','Another content is 2',NULL),('d2bbd408-2836-4c96-92b2-0d44210e8502','One','One content',NULL);
UNLOCK TABLES;


CREATE TABLE `users` (
    `id` char(36) COLLATE utf8_unicode_ci NOT NULL,
    `email` char(100) COLLATE utf8_unicode_ci NOT NULL,
    `full_name` char(100) COLLATE utf8_unicode_ci NOT NULL,
    `password` char(255) NOT NULL,
    PRIMARY KEY (`id`)
);

LOCK TABLES `users` WRITE;
INSERT INTO `users` VALUES ('a3f1c2d4-1234-4abc-9f12-abcdef123456', 'john@example.com', 'John Doe', 'password123'),('b7e2d9f8-5678-4def-8a34-bcdefa654321', 'jane@example.com', 'Jane Smith', 'mypassword');
UNLOCK TABLES;
