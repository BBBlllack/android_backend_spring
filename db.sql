# The name of database is 'andexam', you also can find configuration in application.yml
CREATE TABLE `categories` (
  `categoryId` int NOT NULL,
  `canCreateThread` int DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parentid` int NOT NULL DEFAULT '0',
  `pid` int DEFAULT NULL,
  `property` int DEFAULT NULL,
  `sort` int DEFAULT NULL,
  `threadCount` int DEFAULT NULL,
  PRIMARY KEY (`categoryId`),
  CONSTRAINT `categories_chk_1` CHECK ((`canCreateThread` in (0,1)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `paper` (
  `id` int NOT NULL AUTO_INCREMENT,
  `postid` int NOT NULL,
  `title_zh` text,
  `title_en` text,
  `author` varchar(255) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `detail_en` text,
  `detail_zh` text,
  `createdAt` datetime DEFAULT NULL,
  `pid` int DEFAULT NULL,
  `favor` int NOT NULL DEFAULT '0',
  `pname` varchar(255) DEFAULT NULL,
  `fid` int DEFAULT NULL,
  `fname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32491 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `thread` (
  `threadId` int NOT NULL,
  `postId` int NOT NULL,
  `topicId` int DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `categoryId` int NOT NULL,
  `categoryName` varchar(255) DEFAULT NULL,
  `issueAt` datetime DEFAULT NULL,
  `parentCategoryId` int DEFAULT NULL,
  `parentCategoryName` varchar(255) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `content` longtext,
  PRIMARY KEY (`postId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `rtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`,`password`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
