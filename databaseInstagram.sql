DROP DATABASE IF EXISTS instagram;
CREATE DATABASE instagram;
USE instagram;

CREATE TABLE `user_account` (
    `user_id` varchar(50) PRIMARY KEY ,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `full_name` VARCHAR(100) NOT NULL,
    `profile_picture_url` VARCHAR(255) DEFAULT 'https://i.pinimg.com/236x/08/35/0c/08350cafa4fabb8a6a1be2d9f18f2d88.jpg',
    `bio` varchar(255) DEFAULT NULL,
    `gender`  ENUM('MALE', 'FEMALE', 'OTHER')  DEFAULT 'OTHER',
    `hometown` VARCHAR(255) DEFAULT NULL, 
    `date_of_birth` DATE DEFAULT NULL, 
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` varchar(255),
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE
);


CREATE TABLE `follow` (
    `follow_id` varchar(50) PRIMARY KEY ,
    `follower_id` varchar(50) NOT NULL,  -- Người theo dõiuser_account
    `following_id` varchar(50) NOT NULL, -- Người được theo dõi
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` varchar(255),
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
     UNIQUE (`follower_id`, `following_id`),
    FOREIGN KEY (`follower_id`) REFERENCES `user_account`(`user_id`),
    FOREIGN KEY (`following_id`) REFERENCES `user_account`(`user_id`)
);



CREATE TABLE `post` (
    `post_id` varchar(50) PRIMARY KEY ,
    `user_id`  varchar(50),
    `content` TEXT,
    `privacy` ENUM('PUBLIC', 'PRIVATE', 'FRIENDS')  DEFAULT 'PUBLIC',  
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` varchar(255),
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,	
    FOREIGN KEY (`user_id`) REFERENCES `user_account`(`user_id`)
);

CREATE TABLE `post_file` (
    `file_id` varchar(50) PRIMARY KEY ,
    `post_id`  varchar(50),
    `file_url` VARCHAR(255) NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` varchar(255),
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`post_id`) REFERENCES `post`(`post_id`)
);
CREATE TABLE `post_reaction` (
    `reaction_id`  varchar(50) PRIMARY KEY ,
    `post_id` varchar(50),
    `user_id` varchar(50),
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` varchar(255),
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`post_id`) REFERENCES `post`(`post_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user_account`(`user_id`)
);


CREATE TABLE `post_comment` (
    `comment_id`  varchar(50) PRIMARY KEY ,
    `post_id` varchar(50),                  
    `user_id` varchar(50),                
    `content` TEXT NOT NULL,         
    `parent_comment_id` varchar(50) DEFAULT NULL,  
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP, 
    `created_by` varchar(255), 
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  
    `updated_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE, 
    FOREIGN KEY (`post_id`) REFERENCES `post`(`post_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user_account`(`user_id`),
    FOREIGN KEY (`parent_comment_id`) REFERENCES `post_comment`(`comment_id`)
);

CREATE TABLE `story` (
    `story_id`  varchar(50) PRIMARY KEY ,
    `user_id` varchar(50),
    `type_story` ENUM('FILE', 'TEXT') ,
	`file` VARCHAR(255) DEFAULT NULL,
    `title` varchar(50),
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` varchar(255),
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`user_id`) REFERENCES `user_account`(`user_id`)
);


CREATE TABLE `highlight_story` (
    `story_id`  varchar(50) PRIMARY KEY ,
    `user_id` varchar(50),
    `story_name` VARCHAR(100) NOT NULL, 
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` varchar(255),
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`user_id`) REFERENCES `user_account`(`user_id`)
);

CREATE TABLE `highlight_story_image` (
    `image_id`  varchar(50) PRIMARY KEY ,
    `story_id` varchar(50),  
    `image_url` VARCHAR(255) NOT NULL, 
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` varchar(255),
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` varchar(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`story_id`) REFERENCES `highlight_story`(`story_id`) ON DELETE CASCADE
);

CREATE TABLE `conversation` (
    `conversation_id` VARCHAR(50) PRIMARY KEY,
    `participant1_id` VARCHAR(50) NOT NULL,
    `participant2_id` VARCHAR(50) NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` VARCHAR(255),
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` VARCHAR(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    UNIQUE (`participant1_id`, `participant2_id`), -- Đảm bảo không có cuộc trò chuyện trùng lặp
    FOREIGN KEY (`participant1_id`) REFERENCES `user_account`(`user_id`),
    FOREIGN KEY (`participant2_id`) REFERENCES `user_account`(`user_id`)
);



CREATE TABLE `private_message` (
    `message_id` VARCHAR(50) PRIMARY KEY,
    `conversation_id` VARCHAR(50) NOT NULL,
    `sender_id` VARCHAR(50) NOT NULL,
    `message_content` TEXT NOT NULL,
    `message_type` ENUM('TEXT', 'FILE') NOT NULL,
    `attachment_url` VARCHAR(255),
    `message_status` ENUM('SENT', 'DELIVERED', 'SEEN', 'REQUESTED') DEFAULT 'SENT',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` VARCHAR(255),
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` VARCHAR(255),
    `is_deleted` BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (`conversation_id`) REFERENCES `conversation`(`conversation_id`) ON DELETE CASCADE,
    FOREIGN KEY (`sender_id`) REFERENCES `user_account`(`user_id`)
);
CREATE INDEX conversation_index ON conversation (participant1_id, participant2_id);

