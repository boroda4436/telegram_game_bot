CREATE TABLE IF NOT EXISTS `zayets_bot`.`messages` (
  `message_id` INT NOT NULL AUTO_INCREMENT,
  `text` LONGTEXT NOT NULL,
  `parent_message_id` INT NULL,
  PRIMARY KEY (`message_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'Messages to show for users. Can contain reference to parent message';
