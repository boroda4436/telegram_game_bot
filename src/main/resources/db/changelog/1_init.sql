--liquibase formatted sql

CREATE DATABASE IF NOT  EXISTS `zayets_bot` DEFAULT CHARACTER SET utf8;
GO

CREATE TABLE IF NOT  EXISTS `zayets_bot`.`bot_setting` (
  `bot_setting_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `token` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`bot_setting_id`));
GO

CREATE TABLE IF NOT  EXISTS `messages` (
  `message_id` int(11) NOT NULL AUTO_INCREMENT,
  `text` longtext NOT NULL,
  `parent_message_id` int(11) DEFAULT NULL,
  `bot_setting_id` int(11) NOT NULL,
  PRIMARY KEY (`message_id`),
  KEY `bot_setting_id_idx` (`bot_setting_id`),
  CONSTRAINT `bot_setting_id`
  FOREIGN KEY (`bot_setting_id`)
  REFERENCES `bot_setting` (`bot_setting_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Messages to show for users. Can contain reference to parent message';
GO

