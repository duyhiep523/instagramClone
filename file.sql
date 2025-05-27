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




-- insert dữ liệu


INSERT INTO `user_account` (`user_id`, `username`, `email`, `password`, `full_name`, `profile_picture_url`, `bio`, `gender`, `hometown`, `date_of_birth`, `created_by`) VALUES
('user_001', 'ngoctran_travels', 'ngoc.tran@example.com', '$2a$10$0Wx2TEHGGvhEa7uPOGmAku24AI721CdQBZjagP8BFp0JyfwHSwtke', 'Ngọc Trần', 'https://images.unsplash.com/photo-1517841905240-472988babdf9?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxmZW1hbGUlMjB0cmF2ZWx8ZW58MHx8fHwxNzE2MzI1NDkwfDA&ixlib=rb-4.0.3&q=80&w=400', 'Yêu du lịch và khám phá thế giới!', 'FEMALE', 'Hà Nội', '1995-03-15', 'system'),
('user_002', 'dat_coder_life', 'dat.nguyen@example.com', '$2a$10$0Wx2TEHGGvhEa7uPOGmAku24AI721CdQBZjagP8BFp0JyfwHSwtke', 'Đạt Nguyễn', 'https://i.pinimg.com/236x/08/35/0c/08350cafa4fabb8a6a1be2d9f18f2d88.jpg', 'Coding every day, living the dream.', 'MALE', 'TP. Hồ Chí Minh', '1990-07-22', 'system'),
('user_003', 'mai_art_gallery', 'mai.pham@example.com', '$2a$10$0Wx2TEHGGvhEa7uPOGmAku24AI721CdQBZjagP8BFp0JyfwHSwtke', 'Mai Phạm', 'https://i.pinimg.com/236x/08/35/0c/08350cafa4fabb8a6a1be2d9f18f2d88.jpg', 'Nơi nghệ thuật gặp gỡ tâm hồn.', 'FEMALE', 'Đà Nẵng', '1998-11-01', 'system'),
('user_004', 'tuan_foodie_blog', 'tuan.le@example.com', '$2a$10$0Wx2TEHGGvhEa7uPOGmAku24AI721CdQBZjagP8BFp0JyfwHSwtke', 'Tuấn Lê', 'https://i.pinimg.com/236x/08/35/0c/08350cafa4fabb8a6a1be2d9f18f2d88.jpg', 'Khám phá ẩm thực mọi lúc mọi nơi.', 'MALE', 'Cần Thơ', '1992-04-30', 'system'),
('user_005', 'linh_yoga_life', 'linh.hoang@example.com', '$2a$10$0Wx2TEHGGvhEa7uPOGmAku24AI721CdQBZjagP8BFp0JyfwHSwtke', 'Linh Hoàng', 'https://i.pinimg.com/236x/08/35/0c/08350cafa4fabb8a6a1be2d9f18f2d88.jpg', 'Yoga, thiền định và cuộc sống cân bằng.', 'FEMALE', 'Hải Phòng', '1997-09-10', 'system'),
('user_006', 'hoang_photography', 'hoang.bui@example.com', '$2a$10$0Wx2TEHGGvhEa7uPOGmAku24AI721CdQBZjagP8BFp0JyfwHSwtke', 'Hoàng Bùi', 'https://i.pinimg.com/236x/08/35/0c/08350cafa4fabb8a6a1be2d9f18f2d88.jpg', 'Ghi lại khoảnh khắc qua ống kính.', 'MALE', 'Huế', '1988-01-25', 'system'),
('user_007', 'thuy_fashionista', 'thuy.tran@example.com', '$2a$10$0Wx2TEHGGvhEa7uPOGmAku24AI721CdQBZjagP8BFp0JyfwHSwtke', 'Thúy Trần', 'https://i.pinimg.com/236x/08/35/0c/08350cafa4fabb8a6a1be2d9f18f2d88.jpg', 'Cập nhật xu hướng thời trang mới nhất.', 'FEMALE', 'Nha Trang', '1996-06-05', 'system'),
('user_008', 'minh_gaming_pro', 'minh.phan@example.com', '$2a$10$0Wx2TEHGGvhEa7uPOGmAku24AI721CdQBZjagP8BFp0JyfwHSwtke', 'Minh Phan', 'https://i.pinimg.com/236x/08/35/0c/08350cafa4fabb8a6a1be2d9f18f2d88.jpg', 'Chiến mọi tựa game!', 'MALE', 'Vũng Tàu', '2000-02-14', 'system'),
('user_009', 'anh_bookworm', 'anh.nguyen@example.com', '$2a$10$0Wx2TEHGGvhEa7uPOGmAku24AI721CdQBZjagP8BFp0JyfwHSwtke', 'Ánh Nguyễn', 'https://i.pinimg.com/236x/08/35/0c/08350cafa4fabb8a6a1be2d9f18f2d88.jpg', 'Thế giới trong từng trang sách.', 'FEMALE', 'Đà Lạt', '1993-12-20', 'system'),
('user_010', 'viet_musician', 'viet.le@example.com', '$2a$10$0Wx2TEHGGvhEa7uPOGmAku24AI721CdQBZjagP8BFp0JyfwHSwtke', 'Việt Lê', 'https://i.pinimg.com/236x/08/35/0c/08350cafa4fabb8a6a1be2d9f18f2d88.jpg', 'Sống trọn với âm nhạc.', 'MALE', 'Biên Hòa', '1991-08-08', 'system');


INSERT INTO `follow` (`follow_id`, `follower_id`, `following_id`, `created_by`) VALUES
('f_001', 'user_001', 'user_002', 'system'),
('f_002', 'user_001', 'user_003', 'system'),
('f_003', 'user_002', 'user_001', 'system'),
('f_004', 'user_002', 'user_004', 'system'),
('f_005', 'user_003', 'user_001', 'system'),
('f_006', 'user_003', 'user_005', 'system'),
('f_007', 'user_004', 'user_002', 'system'),
('f_008', 'user_004', 'user_006', 'system'),
('f_009', 'user_005', 'user_003', 'system'),
('f_010', 'user_005', 'user_007', 'system'),
('f_011', 'user_006', 'user_004', 'system'),
('f_012', 'user_006', 'user_008', 'system'),
('f_013', 'user_007', 'user_005', 'system'),
('f_014', 'user_007', 'user_009', 'system'),
('f_015', 'user_008', 'user_006', 'system'),
('f_016', 'user_008', 'user_010', 'system'),
('f_017', 'user_009', 'user_007', 'system'),
('f_018', 'user_009', 'user_001', 'system'),
('f_019', 'user_010', 'user_008', 'system'),
('f_020', 'user_010', 'user_002', 'system'),
('f_021', 'user_001', 'user_004', 'system'),
('f_022', 'user_002', 'user_005', 'system'),
('f_023', 'user_003', 'user_006', 'system'),
('f_024', 'user_004', 'user_001', 'system'),
('f_025', 'user_005', 'user_002', 'system');

INSERT INTO `post` (`post_id`, `user_id`, `content`, `privacy`, `created_at`, `created_by`) VALUES
('post_001', 'user_001', 'Buổi sáng tuyệt vời ở Sapa! #dulich #sapa', 'PUBLIC', '2025-04-20 09:30:00', 'user_001'),
('post_002', 'user_002', 'Hoàn thành dự án lớn! Cảm thấy thật sảng khoái. #coding #project', 'PUBLIC', '2025-04-21 14:00:00', 'user_002'),
('post_003', 'user_003', 'Bức tranh mới toanh, cảm hứng từ biển cả. #art #painting', 'PUBLIC', '2025-04-22 10:15:00', 'user_003'),
('post_004', 'user_004', 'Món phở cuốn ngon bá cháy ở Hà Nội! Phải thử ngay. #foodie #hanoi', 'PUBLIC', '2025-04-23 18:30:00', 'user_004'),
('post_005', 'user_005', 'Một buổi tập yoga đầy năng lượng. #yoga #healthy', 'PUBLIC', '2025-04-24 07:00:00', 'user_005'),
('post_006', 'user_006', 'Hoàng hôn trên vịnh Hạ Long. Đẹp mê hồn! #photography #halongbay', 'PUBLIC', '2025-04-25 17:45:00', 'user_006'),
('post_007', 'user_007', 'Street style mới nhất cho mùa hè. #fashion #summer', 'PUBLIC', '2025-04-26 11:00:00', 'user_007'),
('post_008', 'user_008', 'Vừa đạt top 1 trong game! Feeling good. #gaming #esports', 'PUBLIC', '2025-04-27 21:00:00', 'user_008'),
('post_009', 'user_009', 'Đọc xong cuốn sách này, nhiều cảm xúc quá. #bookworm #reading', 'PUBLIC', '2025-04-28 16:20:00', 'user_009'),
('post_010', 'user_010', 'Tối nay diễn ở quán cafe acoustic. Hẹn gặp mọi người! #music #livemusic', 'PUBLIC', '2025-04-29 19:00:00', 'user_010'),
('post_011', 'user_001', 'Thử thách leo núi ở Fansipan. Cảm giác chinh phục thật tuyệt!', 'PUBLIC', '2025-05-01 08:00:00', 'user_001'),
('post_012', 'user_002', 'Fix bug xong, đi ngủ thôi anh em! 😂 #devlife #debug', 'PUBLIC', '2025-05-02 02:00:00', 'user_002'),
('post_013', 'user_003', 'Hoàn thành tác phẩm điêu khắc đầu tay. #sculpture #artlover', 'PUBLIC', '2025-05-03 13:00:00', 'user_003'),
('post_014', 'user_004', 'Bún đậu mắm tôm chuẩn vị Hà Thành. Ai đói không?', 'PUBLIC', '2025-05-04 12:30:00', 'user_004'),
('post_015', 'user_005', 'Thư giãn với bài tập Hatha yoga. #mindfulness #wellness', 'PUBLIC', '2025-05-05 09:00:00', 'user_005'),
('post_016', 'user_006', 'Chụp ảnh đường phố Sài Gòn về đêm. Đầy màu sắc.', 'PUBLIC', '2025-05-06 20:00:00', 'user_006'),
('post_017', 'user_007', 'Phối đồ đi cafe cuối tuần. Đơn giản mà tinh tế.', 'PUBLIC', '2025-05-07 15:00:00', 'user_007'),
('post_018', 'user_008', 'Stream game Valorant cùng team. High elo here we come!', 'PUBLIC', '2025-05-08 19:30:00', 'user_008'),
('post_019', 'user_009', 'Tìm được một tiệm sách cũ siêu xinh. #books #vintage', 'PUBLIC', '2025-05-09 10:45:00', 'user_009'),
('post_020', 'user_010', 'Sáng tác ca khúc mới. Hy vọng mọi người sẽ thích!', 'PUBLIC', '2025-05-10 14:00:00', 'user_010');




INSERT INTO `post_file` (`file_id`, `post_id`, `file_url`, `created_by`) VALUES
('file_001', 'post_001', 'https://tourdulichsapagiare.com/nview/at_8-diem-den-ly-tuong-o-sapa-vao-buoi-sang_f31e3506411a4cbf2823a082edc14281.png', 'user_001'), -- Sapa Rice Terrace
('file_002', 'post_001', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSXphUBrcLC1jHCrgS2dKLrDLFewaRqv1LSWQ&s', 'user_001'), -- Sapa Mountains
('file_003', 'post_002', 'https://images.unsplash.com/photo-1510519138101-570d1dca3d66?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxjodingJTIwc2V0dXB8ZW58MHx8fHwxNzE2MzI2MDkxfDA&ixlib=rb-4.0.3&q=80&w=400', 'user_002'), -- Coding setup
('file_004', 'post_003', 'https://images.unsplash.com/photo-1579783902671-9f939b7d5178?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxhYnN0cmFjdCUyMHBhaW50aW5nfGVufDB8fHx8MTcxNjMyNjEyN3ww&ixlib=rb-4.0.3&q=80&w=400', 'user_003'), -- Abstract painting
('file_005', 'post_004', 'https://images.unsplash.com/photo-1598514577884-0e318f7a6f23?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHx2aWV0bmFtZXNlJTIwc3ByaW5nJTIwcm9sbHN8ZW58MHx8fHwxNzE2MzI2MTcyfDA&ixlib=rb-4.0.3&q=80&w=400', 'user_004'), -- Vietnamese spring rolls (similar to pho cuon)
('file_006', 'post_005', 'https://images.unsplash.com/photo-1544367352-802521f37e1b?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHlvcGElMjBwb3NlJTIwb3V0ZG9vcnN8ZW58MHx8fHwxNzE2MzI2MjAzfDA&ixlib=rb-4.0.3&q=80&w=400', 'user_005'), -- Yoga pose outdoors
('file_007', 'post_006', 'https://images.unsplash.com/photo-1544772657-3f82fc461d33?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxoYSUyMGxvbmclMjBiYXklMjBzdW5zZXR8ZW58MHx8fHwxNzE2MzI2MjQxfDA&ixlib=rb-4.0.3&q=80&w=400', 'user_006'), -- Ha Long Bay sunset
('file_008', 'post_007', 'https://images.unsplash.com/photo-1596700762145-ee892d2b5120?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxmYXNoaW9uJTIwc3RyZWV0JTIwc3R5bGV8ZW58MHx8fHwxNzE2MzI2Mjg3fDA&ixlib=rb-4.0.3&q=80&w=400', 'user_007'), -- Fashion street style
('file_009', 'post_008', 'https://images.unsplash.com/photo-1596001007802-f3e8f6e8f4b0?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxnYW1pbmclMjBzZXR1cHxlbnwwfHx8fDE3MTYzMjYzNjJ8MA&ixlib=rb-4.0.3&q=80&w=400', 'user_008'), -- Gaming setup
('file_010', 'post_009', 'https://images.unsplash.com/photo-1498146747192-b430c51d6683?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxvcmVuJTIwYm9va3xlbnwwfHx8fDE3MTYzMjYzOTR8MA&ixlib=rb-4.0.3&q=80&w=400', 'user_009'), -- Open book
('file_011', 'post_010', 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd57?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxndWl0YXIlMjBvbiUyMHN0YWdlfGVufDB8fHx8MTcxNjMyNjQ2OXww&ixlib=rb-4.0.3&q=80&w=400', 'user_010'), -- Guitar on stage
('file_012', 'post_011', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtq_Yn1DE1AEJBzTE-l_b-dlnr3ispnMdOhQ&s', 'user_001'), -- Mountain climber on peak
('file_013', 'post_012', 'https://images.unsplash.com/photo-1517454226194-e86976f62f02?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxsYXB0b3AlMjBhbmQlMjBjb2ZmZWV8ZW58MHx8fHwxNzE2MzI2NTMyfDA&ixlib=rb-4.0.3&q=80&w=400', 'user_002'), -- Laptop and coffee
('file_014', 'post_013', 'https://images.unsplash.com/photo-1548842790-2e9102432938?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxzY3VscHR1cmUlMjBhcnR8ZW58MHx8fHwxNzE2MzI2NTc5fDA&ixlib=rb-4.0.3&q=80&w=400', 'user_003'), -- Sculpture art
('file_015', 'post_014', 'https://images.unsplash.com/photo-1627993202958-3d14b434407b?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxoYW5vaSUyMGZvb2R8ZW58MHx8fHwxNzE2MzI2NjY0fDA&ixlib=rb-4.0.3&q=80&w=400', 'user_004'), -- Bun Dau Mam Tom (using a general Hanoi food pic)
('file_016', 'post_015', 'https://images.unsplash.com/photo-1571407914902-124b6794611d?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxlYWx0aHklMjB3b21hbiUyMG1lZGl0YXRpbmd8ZW58MHx8fHwxNzE2MzI2NzEwfDA&ixlib=rb-4.0.3&q=80&w=400', 'user_005'), -- Woman meditating
('file_017', 'post_016', 'https://images.unsplash.com/photo-1517454226194-e86976f62f02?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxzYWlnb24lMjBzdHJlZXRlJTIwYXQlMjBuaWdodHxlbnwwfHx8fDE3MTYzMjY3NDN8MA&ixlib=rb-4.0.3&q=80&w=400', 'user_006'), -- Saigon street at night
('file_018', 'post_017', 'https://images.unsplash.com/photo-1620703816227-2c974917a268?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxjYXN1YWwlMjBvdXRmaXR8ZW58MHx8fHwxNzE2MzI2NzkzfDA&ixlib=rb-4.0.3&q=80&w=400', 'user_007'), -- Casual outfit
('file_019', 'post_018', 'https://images.unsplash.com/photo-1596001007802-f3e8f6e8f4b0?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxnYW1pbmclMjBzY3JlZW58ZW58MHx8fHwxNzE2MzI2ODQxfDA&ixlib=rb-4.0.3&q=80&w=400', 'user_008'), -- Gaming screen
('file_020', 'post_019', 'https://images.unsplash.com/photo-1582236683501-c88f15e7144e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHx2aW50YWdlJTIwYm9va3N0b3JlfGVufDB8fHx8MTcxNjMyNjg4N3ww&ixlib=rb-4.0.3&q=80&w=400', 'user_009'), -- Vintage bookstore
('file_021', 'post_020', 'https://images.unsplash.com/photo-1507874987018-8f83713f01f8?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxtdXNpYyUyMHNoZWV0fGVufDB8fHx8MTcxNjMyNjk0N3ww&ixlib=rb-4.0.3&q=80&w=400', 'user_010'); -- Music sheet



INSERT INTO `post_reaction` (`reaction_id`, `post_id`, `user_id`, `created_by`) VALUES
('react_001', 'post_001', 'user_002', 'user_002'),
('react_002', 'post_001', 'user_003', 'user_003'),
('react_003', 'post_001', 'user_004', 'user_004'),
('react_004', 'post_002', 'user_001', 'user_001'),
('react_005', 'post_002', 'user_003', 'user_003'),
('react_006', 'post_003', 'user_001', 'user_001'),
('react_007', 'post_003', 'user_005', 'user_005'),
('react_008', 'post_004', 'user_001', 'user_001'),
('react_009', 'post_004', 'user_002', 'user_002'),
('react_010', 'post_004', 'user_007', 'user_007'),
('react_011', 'post_005', 'user_003', 'user_003'),
('react_012', 'post_006', 'user_001', 'user_001'),
('react_013', 'post_006', 'user_002', 'user_002'),
('react_014', 'post_007', 'user_005', 'user_005'),
('react_015', 'post_008', 'user_002', 'user_002'),
('react_016', 'post_008', 'user_009', 'user_009'),
('react_017', 'post_009', 'user_007', 'user_007'),
('react_018', 'post_010', 'user_004', 'user_004'),
('react_019', 'post_010', 'user_006', 'user_006'),
('react_020', 'post_011', 'user_002', 'user_002'),
('react_021', 'post_011', 'user_004', 'user_004'),
('react_022', 'post_012', 'user_001', 'user_001'),
('react_023', 'post_013', 'user_001', 'user_001'),
('react_024', 'post_014', 'user_003', 'user_003'),
('react_025', 'post_015', 'user_002', 'user_002'),
('react_026', 'post_016', 'user_001', 'user_001'),
('react_027', 'post_017', 'user_004', 'user_004'),
('react_028', 'post_018', 'user_001', 'user_001'),
('react_029', 'post_019', 'user_002', 'user_002'),
('react_030', 'post_020', 'user_003', 'user_003'),
('react_031', 'post_001', 'user_005', 'user_005'),
('react_032', 'post_002', 'user_006', 'user_006'),
('react_033', 'post_003', 'user_007', 'user_007'),
('react_034', 'post_004', 'user_008', 'user_008'),
('react_035', 'post_005', 'user_009', 'user_009');




INSERT INTO `post_comment` (`comment_id`, `post_id`, `user_id`, `content`, `parent_comment_id`, `created_by`) VALUES
('cmt_001', 'post_001', 'user_002', 'Tuyệt vời quá Ngọc ơi! Cảnh Sapa đẹp thật.', NULL, 'user_002'),
('cmt_002', 'post_001', 'user_003', 'Ảnh đẹp lắm!', NULL, 'user_003'),
('cmt_003', 'post_001', 'user_004', 'Nhìn mà muốn đi ngay!', NULL, 'user_004'),
('cmt_004', 'post_001', 'user_001', 'Cảm ơn mọi người nha! Sapa đúng là đáng để đi.', 'cmt_001', 'user_001'),
('cmt_005', 'post_002', 'user_001', 'Chúc mừng Đạt nhé! Năng suất quá.', NULL, 'user_001'),
('cmt_006', 'post_002', 'user_005', 'Ghê ghê! Project gì mà xịn vậy?', NULL, 'user_005'),
('cmt_007', 'post_003', 'user_001', 'Ấn tượng quá Mai! Màu sắc và bố cục rất hài hòa.', NULL, 'user_001'),
('cmt_008', 'post_003', 'user_006', 'Một kiệt tác!', NULL, 'user_006'),
('cmt_009', 'post_004', 'user_001', 'Nhìn thèm quá Tuấn ơi!', NULL, 'user_001'),
('cmt_010', 'post_004', 'user_007', 'Địa chỉ ở đâu vậy bạn?', NULL, 'user_007'),
('cmt_011', 'post_004', 'user_004', 'Ở phố Hàng Điếu nha bạn!', 'cmt_010', 'user_004'),
('cmt_012', 'post_005', 'user_003', 'Linh đẹp quá! Mình cũng muốn tập yoga.', NULL, 'user_003'),
('cmt_013', 'post_006', 'user_001', 'Hoàng hôn Ha Long đúng là huyền ảo.', NULL, 'user_001'),
('cmt_014', 'post_007', 'user_005', 'Thúy mặc đồ đẹp ghê!', NULL, 'user_005'),
('cmt_015', 'post_008', 'user_002', 'Pro quá Minh ơi! Khi nào chỉ mình với.', NULL, 'user_002'),
('cmt_016', 'post_008', 'user_008', 'Ok la bro!', 'cmt_015', 'user_008'),
('cmt_017', 'post_009', 'user_007', 'Tên sách gì vậy Ánh?', NULL, 'user_007'),
('cmt_018', 'post_009', 'user_009', 'Là "Rừng Na Uy" đó bạn!', 'cmt_017', 'user_009'),
('cmt_019', 'post_010', 'user_004', 'Tuyệt vời Việt! Chúc show diễn thành công.', NULL, 'user_004'),
('cmt_020', 'post_011', 'user_002', 'Leo Fansipan đỉnh thật!', NULL, 'user_002'),
('cmt_021', 'post_012', 'user_001', 'Good job Đạt!', NULL, 'user_001'),
('cmt_022', 'post_013', 'user_001', 'Xuất sắc Mai!', NULL, 'user_001'),
('cmt_023', 'post_014', 'user_003', 'Thèm bún đậu quá!', NULL, 'user_003'),
('cmt_024', 'post_015', 'user_002', 'Khỏe mạnh ghê Linh!', NULL, 'user_002'),
('cmt_025', 'post_016', 'user_001', 'Ảnh đêm Sài Gòn có hồn lắm Hoàng.', NULL, 'user_001'),
('cmt_026', 'post_017', 'user_004', 'Outfit này đẹp lắm Thúy!', NULL, 'user_004'),
('cmt_027', 'post_018', 'user_001', 'Gosu!', NULL, 'user_001'),
('cmt_028', 'post_019', 'user_002', 'Sách cũ luôn có nét riêng.', NULL, 'user_002'),
('cmt_029', 'post_020', 'user_003', 'Hóng bài hát mới của Việt.', NULL, 'user_003'),
('cmt_030', 'post_001', 'user_005', 'Sapa mùa này đẹp lắm!', 'cmt_004', 'user_005');



INSERT INTO `story` (`story_id`, `user_id`, `type_story`, `file`, `title`, `created_by`) VALUES
('story_001', 'user_001', 'FILE', 'https://images.unsplash.com/photo-1544837456-42d48092a0e4?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxzYXBhJTIwcmljZSUyMHRlcnJhY2V8ZW58MHx8fHwxNzE2MzI2MDM0fDA&ixlib=rb-4.0.3&q=80&w=400', 'Sapa Sáng Sớm', 'user_001'),
('story_002', 'user_002', 'TEXT', NULL, 'Ngày làm việc hiệu quả!', 'user_002'),
('story_003', 'user_003', 'FILE', 'https://images.unsplash.com/photo-1579783902671-9f939b7d5178?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxhYnN0cmFjdCUyMHBhaW50aW5nfGVufDB8fHx8MTcxNjMyNjEyN3ww&ixlib=rb-4.0.3&q=80&w=400', 'Phác Thảo Mới', 'user_003'),
('story_004', 'user_004', 'FILE', 'https://images.unsplash.com/photo-1497935639147-380104192ddf?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxjYWZlJTIwcGhvdG98ZW58MHx8fHwxNzE2MzI3MDA2fDA&ixlib=rb-4.0.3&q=80&w=400', 'Cafe chiều Hà Nội', 'user_004'),
('story_005', 'user_005', 'TEXT', NULL, 'Thử thách 30 ngày Yoga', 'user_005'),
('story_006', 'user_006', 'FILE', 'https://images.unsplash.com/photo-1506748686214-e9df1e1d3e8e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxhbmglMjBkZW4lMjB0aGFuaCUyMHBobyV8ZW58MHx8fHwxNzE2MzI3MDc2fDA&ixlib=rb-4.0.3&q=80&w=400', 'Ánh đèn thành phố', 'user_006'),
('story_007', 'user_001', 'FILE', 'https://images.unsplash.com/photo-1508214751196-eedd8ccb90b8?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxkYSUyMG5hbmclMjBiZWFjaHxlbnwwfHx8fDE3MTYzMjcxMTZ8MA&ixlib=rb-4.0.3&q=80&w=400', 'Kỷ niệm Đà Nẵng', 'user_001'),
('story_008', 'user_008', 'TEXT', NULL, 'Livestream game tối nay!', 'user_008');


INSERT INTO `highlight_story` (`story_id`, `user_id`, `story_name`, `created_by`) VALUES
('highlight_001', 'user_001', 'Du Lịch', 'user_001'),
('highlight_002', 'user_003', 'Nghệ Thuật Của Tôi', 'user_003'),
('highlight_003', 'user_005', 'Yoga Journey', 'user_005');


INSERT INTO `highlight_story_image` (`image_id`, `story_id`, `image_url`, `created_by`) VALUES
('hs_img_001', 'highlight_001', 'https://images.unsplash.com/photo-1544837456-42d48092a0e4?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxzYXBhJTIwcmljZSUyMHRlcnJhY2V8ZW58MHx8fHwxNzE2MzI2MDM0fDA&ixlib=rb-4.0.3&q=80&w=400', 'user_001'), -- Sapa Story
('hs_img_002', 'highlight_001', 'https://images.unsplash.com/photo-1628178122394-4d2d6c6e7e1e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxzYXBhJTIwbW91bnRhaW5zfGVufDB8fHx8MTcxNjMyNjA2OHww&ixlib=rb-4.0.3&q=80&w=400', 'user_001'), -- Sapa Post File
('hs_img_003', 'highlight_002', 'https://images.unsplash.com/photo-1579783902671-9f939b7d5178?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxhYnN0cmFjdCUyMHBhaW50aW5nfGVufDB8fHx8MTcxNjMyNjEyN3ww&ixlib=rb-4.0.3&q=80&w=400', 'user_003'),
('hs_img_004', 'highlight_002', 'https://images.unsplash.com/photo-1548842790-2e9102432938?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHxzY3VscHR1cmUlMjBhcnR8ZW58MHx8fHwxNzE2MzI2NTc5fDA&ixlib=rb-4.0.3&q=80&w=400', 'user_003'),
('hs_img_005', 'highlight_003', 'https://images.unsplash.com/photo-1544367352-802521f37e1b?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w1ODU1NzB8MHwxfHNlYXJjaHwxfHlvcGElMjBwb3NlJTIwb3V0ZG9vcnN8ZW58MHx8fHwxNzE2MzI2MjAzfDA&ixlib=rb-4.0.3&q=80&w=400', 'user_005');





