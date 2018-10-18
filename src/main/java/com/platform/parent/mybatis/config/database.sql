#课程
DROP TABLE IF EXISTS course;
CREATE TABLE `course` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` text NOT NULL,
  `subtitle` text NOT NULL,
  `description` text,
  `passrate` int(11) DEFAULT NULL,
  `teachers` text,
  `ranking` int(11) DEFAULT NULL,
  `audience` text,
  `goal` text,
  `outline` text,
  `highlight` text,
  `level` int(11) DEFAULT NULL,
  `videoURL` text,
  `videoSize` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

#拼团
DROP TABLE IF EXISTS grouppurchase;
CREATE TABLE `grouppurchase` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `courseId` bigint(20) NOT NULL,
  `owner` text,
  `membersCount` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deadline` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `realDeadline` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

#团员
DROP TABLE IF EXISTS grouppurchasemember;
CREATE TABLE `grouppurchasemember` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `wechatid` text NOT NULL,
  `groupId` bigint(20) NOT NULL,
  `joined` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8;

DROP VIEW IF EXISTS grouppurchase_view;
CREATE VIEW `grouppurchase_view` AS
    SELECT 
        `grouppurchase`.`id` AS `id`,
        `course`.`id` AS `courseId`,
        `course`.`title` AS `courseTitle`,
        `grouppurchasemember`.`wechatid` AS `ownerUnionid`,
        `grouppurchase`.`membersCount` AS `membersCount`,
        `grouppurchase`.`price` AS `price`,
        `grouppurchase`.`created` AS `created`,
        `grouppurchase`.`deadline` AS `deadline`,
        `grouppurchase`.`realDeadline` AS `realdeadline`,
        `grouppurchase`.`status` AS `status`
    FROM
        ((`grouppurchase`
        JOIN `course` ON ((`grouppurchase`.`courseId` = `course`.`id`)))
        JOIN `grouppurchasemember` ON (((`grouppurchasemember`.`groupId` = `grouppurchase`.`id`)
            AND (`grouppurchase`.`owner` = `grouppurchasemember`.`wechatid`))));