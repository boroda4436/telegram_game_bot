CREATE TABLE IF NOT  EXISTS `zayets_bot`.`bot_setting` (
  `bot_setting_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `token` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`bot_setting_id`));
