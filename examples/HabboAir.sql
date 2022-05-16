SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- Users Settings
ALTER TABLE `users_settings`
  ADD `star_gems` INT(11) NOT NULL DEFAULT '0' AFTER `guild_id`;


-- Emulator stuff
INSERT INTO `emulator_texts` (`key`, `value`) VALUES ('stargem.invalid.target', 'Your target was invalid! Sorry :(');
INSERT INTO `emulator_texts` (`key`, `value`) VALUES ('stargem.received.from', 'You received a gem from %username%!');
INSERT INTO `emulator_texts` (`key`, `value`) VALUES ('stargem.not.enough', 'Sorry, you don\'t have enough Duckets to give Stargems!');

INSERT INTO `emulator_settings` (`key`, `value`) VALUES ('stargem.amount', '1');
INSERT INTO `emulator_settings` (`key`, `value`) VALUES ('stargem.currency.type', '0');
INSERT INTO `emulator_settings` (`key`, `value`) VALUES ('stargem.give.currency', '0');


-- Achievements + basic cleanup
ALTER TABLE `achievements`
ADD `state` smallint(4) NOT NULL DEFAULT '1' COMMENT '0 = disabled, 1 = enabled, 2 = archive';


-- Hot looks
DROP TABLE IF EXISTS `hotlooks`;
CREATE TABLE `hotlooks`  (
  `look` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'The look users can pick.',
  `gender` enum('F','M') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'M',
  PRIMARY KEY (`look`) USING BTREE,
  UNIQUE INDEX `Primary key`(`look`, `gender`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

INSERT INTO `hotlooks` VALUES ('hd-180-8.ch-225-64.lg-285-1408.sh-300-64.ha-1023-64', 'M');
INSERT INTO `hotlooks` VALUES ('hd-185-30.ch-225-64.lg-285-1408.sh-300-64.ha-1023-1408.wa-2007', 'M');
INSERT INTO `hotlooks` VALUES ('hd-195-14.ch-210-81.lg-270-71.ha-1003-1408', 'M');
INSERT INTO `hotlooks` VALUES ('hd-629-1370.ch-680-85.lg-720-64.ha-1022-85', 'F');
INSERT INTO `hotlooks` VALUES ('hr-105-40.hd-190-10.ch-240-1408.lg-270-1408.sh-300-1408.fa-1206-1408.wa-2009-1408.cc-3280-1408-1408', 'M');
INSERT INTO `hotlooks` VALUES ('hr-110-45.hd-180-30.ch-3030-91.lg-275-90.ha-1015.ea-1406.fa-1212.wa-3211-64-64', 'M');
INSERT INTO `hotlooks` VALUES ('hr-115-42.hd-180-1370.ch-804-1408.lg-285-81.sh-290-1408.ha-1002-1408.fa-1201', 'M');
INSERT INTO `hotlooks` VALUES ('hr-125-35.hd-190-10.ch-240-81.lg-285-91.sh-300-64.ha-1004-1408', 'M');
INSERT INTO `hotlooks` VALUES ('hr-125-44.hd-195-10.ch-255-1408.lg-275-73.sh-295-1408.ha-1003-73.fa-1201', 'M');
INSERT INTO `hotlooks` VALUES ('hr-155-37.hd-190-14.ch-225-82.lg-285-1408.sh-300-64', 'M');
INSERT INTO `hotlooks` VALUES ('hr-155-45.hd-180-1.ch-210-82.lg-275-64.sh-300-1408', 'M');
INSERT INTO `hotlooks` VALUES ('hr-3090-39.hd-209-1.ch-806-73.lg-3216-73.sh-908-73.he-3274-73.ea-1404-64.ca-1813.wa-3211-73-1408', 'M');
INSERT INTO `hotlooks` VALUES ('hr-3090-45.hd-180-1370.ch-235-68.lg-3023-91.sh-300-64.ha-1013-91.wa-3211-91-66', 'M');
INSERT INTO `hotlooks` VALUES ('hr-3090-45.hd-180-19.ch-876-81-1408.lg-280-1320.sh-295-90.fa-3276-73.wa-3074-91-90', 'M');
INSERT INTO `hotlooks` VALUES ('hr-515-31.hd-600-1.ch-818-1408.lg-695-80.sh-725-1408.ha-3117-1408.ca-1819', 'F');
INSERT INTO `hotlooks` VALUES ('hr-515-31.hd-600-10.ch-630-76.lg-695-64.sh-730-91.ea-1401-64', 'F');
INSERT INTO `hotlooks` VALUES ('hr-515-31.hd-600-1370.ch-665-66.lg-3216-66.sh-905-1408.he-1610', 'F');
INSERT INTO `hotlooks` VALUES ('hr-515-34.hd-600-14.ch-630-1408.lg-700-1408.sh-725-1408.he-1601', 'F');
INSERT INTO `hotlooks` VALUES ('hr-515-34.hd-600-8.ch-884-68.lg-715-71.sh-907-64.ha-3298-71-73.wa-3210-71-71', 'F');
INSERT INTO `hotlooks` VALUES ('hr-515-44.hd-600-14.ch-884-84.lg-695-1408.sh-735-91.ea-1406', 'F');
INSERT INTO `hotlooks` VALUES ('hr-515-45.hd-600-1370.ch-630-71.lg-700-1408.sh-730-1408.he-1610.ca-1801-1408', 'F');
INSERT INTO `hotlooks` VALUES ('hr-540-32.hd-629-1.ch-685-81.lg-710-81.he-3274-1408', 'F');
INSERT INTO `hotlooks` VALUES ('hr-540-45.hd-625-1370.ch-655-64.lg-3023-64.fa-3276-91', 'F');
INSERT INTO `hotlooks` VALUES ('hr-545-48.hd-600-1.ch-680-73.lg-710-82.sh-3068-64-1408.ha-1005-84.cp-3288-64', 'F');
INSERT INTO `hotlooks` VALUES ('hr-575-39.hd-615-1.ch-660-64.lg-3023-73.sh-740-1408.ha-1002-64.he-1609-64.ea-1404-64.fa-1212.ca-1809.wa-2001', 'F');
INSERT INTO `hotlooks` VALUES ('hr-676-45.hd-190-1371.ch-235-73.lg-270-82.sh-3068-64-1408', 'M');
INSERT INTO `hotlooks` VALUES ('hr-679-39.hd-600-14.ch-669-1408.lg-3116-73-1408.sh-906-71.ha-1003-71.he-1605-81.ca-1818', 'F');
INSERT INTO `hotlooks` VALUES ('hr-679-45.hd-600-1.ch-655-72.lg-710-82.sh-740-71.ha-1004-72', 'F');
INSERT INTO `hotlooks` VALUES ('hr-802-31.hd-180-1.ch-255-72.lg-3078-85.sh-290-64.wa-2001', 'M');
INSERT INTO `hotlooks` VALUES ('hr-802-31.hd-209-10.ch-255-73.lg-3290-64.sh-906-73', 'M');
INSERT INTO `hotlooks` VALUES ('hr-802-45.hd-190-1.ch-3030-82.lg-281-72.sh-300-64.wa-2007', 'M');
INSERT INTO `hotlooks` VALUES ('hr-837-39.hd-627-10.ch-685-73.lg-705-73.sh-735-66', 'F');
INSERT INTO `hotlooks` VALUES ('hr-889-34.hd-180-1.ch-3030-81.lg-3023-85.sh-290-73.fa-1201.cc-3294-64-66', 'M');
INSERT INTO `hotlooks` VALUES ('hr-890-32.hd-600-10.ch-822-66.lg-696-1408.sh-907-72', 'F');
INSERT INTO `hotlooks` VALUES ('hr-890-34.hd-600-1.ch-884-73.lg-3216-1408.sh-907-73.he-1610\" hash=\"3200b8950723cdd549e2ef259719b9c3', 'F');
INSERT INTO `hotlooks` VALUES ('hr-890-42.hd-629-1.ch-670-64.lg-705-1408.sh-907-64', 'F');
INSERT INTO `hotlooks` VALUES ('hr-890-48.hd-629-1.ch-665-66.lg-3023-66.sh-3068-85-1408.he-1602-66.ca-1812', 'F');
INSERT INTO `hotlooks` VALUES ('hr-893-37.hd-180-1371.ch-250-91.lg-3023-64.sh-290-1408', 'M');
INSERT INTO `hotlooks` VALUES ('hr-893-45.hd-209-1371.ch-255-1408.lg-275-1408.sh-908-90', 'M');

SET FOREIGN_KEY_CHECKS = 1;
