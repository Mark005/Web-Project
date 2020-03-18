-- -----------------------------------------------------
-- Schema usersDb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `usersDb` DEFAULT CHARACTER SET utf8 ;
USE `usersDb` ;

-- -----------------------------------------------------
-- Table `usersDb`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usersDb`.`User` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surename` VARCHAR(45) NOT NULL,
  `gender` VARCHAR(45) NULL,
  `country` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `usersDb`.`UserCreds`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usersDb`.`UserCreds` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `pass` VARCHAR(45) NOT NULL,
  `user_fk` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`login`),
  CONSTRAINT `user_fk`
    FOREIGN KEY (`user_fk`)
    REFERENCES `usersDb`.`User` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Data for table `usersDb`.`User`
-- -----------------------------------------------------
START TRANSACTION;
USE `usersDb`;
INSERT INTO `usersDb`.`User` (`id`, `name`, `surename`, `gender`, `country`) VALUES (DEFAULT, 'Tom', 'White', 'male', 'Belarus');
INSERT INTO `usersDb`.`User` (`id`, `name`, `surename`, `gender`, `country`) VALUES (DEFAULT, 'Catrin', 'Paul', 'female', 'USA');
INSERT INTO `usersDb`.`User` (`id`, `name`, `surename`, `gender`, `country`) VALUES (DEFAULT, 'Antony', 'Recker', 'male', 'Germany');
INSERT INTO `usersDb`.`User` (`id`, `name`, `surename`, `gender`, `country`) VALUES (DEFAULT, 'Margaret', 'Nory', 'female', 'Italy');

COMMIT;


-- -----------------------------------------------------
-- Data for table `usersDb`.`UserCreds`
-- -----------------------------------------------------
START TRANSACTION;
USE `usersDb`;
INSERT INTO `usersDb`.`UserCreds` (`id`, `login`, `pass`, `user_fk`) VALUES (DEFAULT, 'login', '1233', 1);
INSERT INTO `usersDb`.`UserCreds` (`id`, `login`, `pass`, `user_fk`) VALUES (DEFAULT, 'aaa', '456', 2);
INSERT INTO `usersDb`.`UserCreds` (`id`, `login`, `pass`, `user_fk`) VALUES (DEFAULT, 'bbb', '789', 3);
INSERT INTO `usersDb`.`UserCreds` (`id`, `login`, `pass`, `user_fk`) VALUES (DEFAULT, 'ccc', '159', 4);

COMMIT;

