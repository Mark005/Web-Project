-- -----------------------------------------------------
-- Schema companyDb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `companyDb` DEFAULT CHARACTER SET utf8 ;
USE `companyDb` ;

-- -----------------------------------------------------
-- Table `companyDb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `companyDb`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `certificate_number` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `gender` VARCHAR(45) NOT NULL,
  `profession` VARCHAR(45) NOT NULL,
  `date_employment` DATE NOT NULL,
  `isadmin` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`certificate_number`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `companyDb`.`user_creds`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `companyDb`.`user_creds` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `pass` VARCHAR(45) NOT NULL,
  `user_fk` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`login`),
  KEY (`user_fk`),
  CONSTRAINT `user_fk`
    FOREIGN KEY (`user_fk`)
    REFERENCES `companyDb`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `companyDb`.`briefing`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `companyDb`.`briefing` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(250) NOT NULL,
  `interval_in_months` SMALLINT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `companyDb`.`user_has_briefing`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `companyDb`.`user_has_briefing` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `last_date` DATE NULL,
  `briefing_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`briefing_id`,`user_id`),
  INDEX `fk_User_has_briefing_briefing1_idx` (`briefing_id` ASC) VISIBLE,
  CONSTRAINT `fk_User_has_briefing_User1`
    FOREIGN KEY (`user_id`)
    REFERENCES `companyDb`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_User_has_briefing_briefing1`
    FOREIGN KEY (`briefing_id`)
    REFERENCES `companyDb`.`briefing` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `companyDb`.`task`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `companyDb`.`task` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(250) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `deadline` DATE NULL,
  `creator` INT NOT NULL,
  `executor` INT NOT NULL,
  PRIMARY KEY (`id`),
  KEY (`creator`),
  KEY (`executor`),
  CONSTRAINT `fk_personal_task_User1`
    FOREIGN KEY (`creator`)
    REFERENCES `companyDb`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_task_User1`
    FOREIGN KEY (`executor`)
    REFERENCES `companyDb`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `companyDb`.`message`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `companyDb`.`message` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `text` VARCHAR(500) NULL,
  `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_from` INT NOT NULL,
  `User_to` INT NULL,
  PRIMARY KEY (`id`),
  KEY (`user_from`),
  KEY (`User_to`),
  CONSTRAINT `fk_message_User1`
    FOREIGN KEY (`user_from`)
    REFERENCES `companyDb`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_message_User2`
    FOREIGN KEY (`User_to`)
    REFERENCES `companyDb`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Data for table `companyDb`.`user`
-- -----------------------------------------------------
START TRANSACTION;
USE `companyDb`;
INSERT INTO `companyDb`.`user` (`id`, `certificate_number`, `name`, `surname`, `gender`, `profession`, `date_employment`, `isadmin`) VALUES (DEFAULT, 123, 'Tom', 'White', 'MALE', 'labour protection engineer', '2010-06-01', true);
INSERT INTO `companyDb`.`user` (`id`, `certificate_number`, `name`, `surname`, `gender`, `profession`, `date_employment`, `isadmin`) VALUES (DEFAULT, 456, 'Catrin', 'Paul', 'FEMALE', 'assembler', '2011-07-01', false);
INSERT INTO `companyDb`.`user` (`id`, `certificate_number`, `name`, `surname`, `gender`, `profession`, `date_employment`, `isadmin`) VALUES (DEFAULT, 789, 'Antony', 'Recker', 'MALE', 'welder', '2018-02-15', false);
INSERT INTO `companyDb`.`user` (`id`, `certificate_number`, `name`, `surname`, `gender`, `profession`, `date_employment`, `isadmin`) VALUES (DEFAULT, 159, 'Margaret', 'Nory', 'FEMALE', 'electrician', '2016-06-21', false);
INSERT INTO `companyDb`.`user` (`id`, `certificate_number`, `name`, `surname`, `gender`, `profession`, `date_employment`, `isadmin`) VALUES (DEFAULT, 753, 'Tonny', 'Verdeno', 'MALE', 'engineer', '2020-03-01', false);

COMMIT;


-- -----------------------------------------------------
-- Data for table `companyDb`.`user_creds`
-- -----------------------------------------------------
START TRANSACTION;
USE `companyDb`;
INSERT INTO `companyDb`.`user_creds` (`id`, `login`, `pass`, `user_fk`) VALUES (DEFAULT, 'admin', 'admin', 1);
INSERT INTO `companyDb`.`user_creds` (`id`, `login`, `pass`, `user_fk`) VALUES (DEFAULT, 'aaa', '456', 2);
INSERT INTO `companyDb`.`user_creds` (`id`, `login`, `pass`, `user_fk`) VALUES (DEFAULT, 'bbb', '789', 3);
INSERT INTO `companyDb`.`user_creds` (`id`, `login`, `pass`, `user_fk`) VALUES (DEFAULT, 'ccc', '159', 4);
INSERT INTO `companyDb`.`user_creds` (`id`, `login`, `pass`, `user_fk`) VALUES (DEFAULT, 'user', 'user', 5);

COMMIT;


-- -----------------------------------------------------
-- Data for table `companyDb`.`briefing`
-- -----------------------------------------------------
START TRANSACTION;
USE `companyDb`;
INSERT INTO `companyDb`.`briefing` (`id`, `name`, `interval_in_months`) VALUES (DEFAULT, 'safety instruction', 6);
INSERT INTO `companyDb`.`briefing` (`id`, `name`, `interval_in_months`) VALUES (DEFAULT, 'work with power tools', 12);
INSERT INTO `companyDb`.`briefing` (`id`, `name`, `interval_in_months`) VALUES (DEFAULT, 'loading and unloading', 12);
INSERT INTO `companyDb`.`briefing` (`id`, `name`, `interval_in_months`) VALUES (DEFAULT, 'work with a brush cutter', 12);
INSERT INTO `companyDb`.`briefing` (`id`, `name`, `interval_in_months`) VALUES (DEFAULT, 'work with a lawn mower', 12);
INSERT INTO `companyDb`.`briefing` (`id`, `name`, `interval_in_months`) VALUES (DEFAULT, 'work at height', 12);

COMMIT;


-- -----------------------------------------------------
-- Data for table `companyDb`.`user_has_briefing`
-- -----------------------------------------------------
START TRANSACTION;
USE `companyDb`;
INSERT INTO `companyDb`.`user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2020-03-12', 1, 1);
INSERT INTO `companyDb`.`user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2019-03-12', 2, 1);
INSERT INTO `companyDb`.`user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2020-03-12', 1, 2);
INSERT INTO `companyDb`.`user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2019-12-28', 4, 2);
INSERT INTO `companyDb`.`user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2020-01-28', 1, 3);
INSERT INTO `companyDb`.`user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2019-08-21', 1, 4);
INSERT INTO `companyDb`.`user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2019-02-17', 4, 4);
INSERT INTO `companyDb`.`user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2019-07-28', 5, 4);
INSERT INTO `companyDb`.`user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2019-12-28', 1, 5);
INSERT INTO `companyDb`.`user_has_briefing` (`id`, `last_date`, `briefing_id`, `user_id`) VALUES (DEFAULT, '2019-05-12', 3, 5);

COMMIT;


-- -----------------------------------------------------
-- Data for table `companyDb`.`task`
-- -----------------------------------------------------
START TRANSACTION;
USE `companyDb`;
INSERT INTO `companyDb`.`task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something one', 'PERSONAL', 'OPEN', '2020-07-15', 1, 2);
INSERT INTO `companyDb`.`task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something two', 'ELECTRONIC', 'CLOSE', NULL, 1, 3);
INSERT INTO `companyDb`.`task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something three', 'WELDING', 'EXECUTING', NULL, 1, 4);
INSERT INTO `companyDb`.`task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something four', 'ADJUSTMENT', 'OPEN', NULL, 1, 5);
INSERT INTO `companyDb`.`task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something five', 'PERSONAL', 'OPEN', '2020-09-03', 1, 2);
INSERT INTO `companyDb`.`task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something six', 'ELECTRONIC', 'OPEN', '2020-05-28', 1, 3);
INSERT INTO `companyDb`.`task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something seven', 'WELDING', 'CLOSE', '2020-03-19', 1, 4);
INSERT INTO `companyDb`.`task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something eight', 'ADJUSTMENT', 'CLOSE', '2020-02-11', 1, 5);
INSERT INTO `companyDb`.`task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something nine', 'PERSONAL', 'CLOSE', '2020-03-19', 1, 2);
INSERT INTO `companyDb`.`task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something ten', 'ELECTRONIC', 'EXECUTING', '2020-05-19', 1, 3);
INSERT INTO `companyDb`.`task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something eleven', 'WELDING', 'EXECUTING', '2020-06-03', 1, 4);
INSERT INTO `companyDb`.`task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something twelve', 'ADJUSTMENT', 'EXECUTING', '2020-06-12', 1, 5);
INSERT INTO `companyDb`.`task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something thirteen', 'WELDING', 'OPEN', '2020-03-19', 1, 2);
INSERT INTO `companyDb`.`task` (`id`, `name`, `type`, `status`, `deadline`, `creator`, `executor`) VALUES (DEFAULT, 'to do something fourteen', 'PERSONAL', 'EXECUTING', '2020-05-19', 1, 3);


COMMIT;


-- -----------------------------------------------------
-- Data for table `companyDb`.`message`
-- -----------------------------------------------------
START TRANSACTION;
USE `companyDb`;
INSERT INTO `companyDb`.`message` (`id`, `text`, `date`, `user_from`, `User_to`) VALUES (DEFAULT, 'hi', '2020-03-20 20:20:20', 1, 4);
INSERT INTO `companyDb`.`message` (`id`, `text`, `date`, `user_from`, `User_to`) VALUES (DEFAULT, 'hi', '2020-03-20 20:21:00', 4, 1);
INSERT INTO `companyDb`.`message` (`id`, `text`, `date`, `user_from`, `User_to`) VALUES (DEFAULT, 'what\'s up?', '2020-03-20 20:22:00', 1, 4);
INSERT INTO `companyDb`.`message` (`id`, `text`, `date`, `user_from`, `User_to`) VALUES (DEFAULT, 'nice', '2020-03-20 20:23:00', 4, 1);
INSERT INTO `companydb`.`message` (`text`, `date`, `user_from`) VALUES ('common', '2020-02-21 20:23:00', '2');
INSERT INTO `companydb`.`message` (`text`, `date`, `user_from`) VALUES ('chat', '2020-02-21 20:24:00', '3');
INSERT INTO `companydb`.`message` (`text`, `date`, `user_from`) VALUES ('messages', '2020-02-21 20:25:00', '5');

COMMIT;

