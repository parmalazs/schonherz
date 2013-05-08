SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE SCHEMA IF NOT EXISTS `flottadb` DEFAULT CHARACTER utf8 ;
USE `mydb` ;
USE `flottadb` ;

-- -----------------------------------------------------
-- Table `flottadb`.`profilkep`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `flottadb`.`profilkep` (
  `profilKepID` INT(11) NOT NULL AUTO_INCREMENT ,
  `profilKepPath` TEXT NULL DEFAULT NULL ,
  `profilKepDateTime` TEXT NULL DEFAULT NULL ,
  `soforID` INT(11) NULL DEFAULT NULL ,
  PRIMARY KEY (`profilKepID`) ,
  INDEX `Sofor_ProfilKep_CON` (`soforID` ASC) ,
  CONSTRAINT `Sofor_ProfilKep_CON`
    FOREIGN KEY (`soforID` )
    REFERENCES `flottadb`.`sofor` (`soforID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `flottadb`.`sofor`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `flottadb`.`sofor` (
  `soforID` INT(11) NOT NULL  AUTO_INCREMENT ,
  `soforNev` TEXT NULL DEFAULT NULL ,
  `soforCim` TEXT NULL DEFAULT NULL ,
  `soforTelefonszam` TEXT NULL DEFAULT NULL ,
  `soforLogin` TEXT NULL DEFAULT NULL ,
  `soforPass` TEXT NULL DEFAULT NULL ,
  `soforBirthDate` TEXT NULL DEFAULT NULL ,
  `soforRegTime` TEXT NULL DEFAULT NULL ,
  `soforIsAdmin` TINYINT(1) NULL DEFAULT NULL ,
  `soforEmail` TEXT NULL DEFAULT NULL ,
  `soforProfilKepID` INT(11) NULL DEFAULT NULL ,
  PRIMARY KEY (`soforID`) ,
  INDEX `fk_sofor_profilkep1_idx` (`soforProfilKepID` ASC) ,
  CONSTRAINT `fk_sofor_profilkep1`
    FOREIGN KEY (`soforProfilKepID` )
    REFERENCES `flottadb`.`profilkep` (`profilKepID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `flottadb`.`autokep`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `flottadb`.`autokep` (
  `autoKepID` INT(11) NOT NULL  AUTO_INCREMENT ,
  `autoKepPath` TEXT NULL DEFAULT NULL ,
  `autoKepDateTime` TEXT NULL DEFAULT NULL ,
  `autoID` INT(11) NOT NULL ,
  PRIMARY KEY (`autoKepID`) ,
  INDEX `fk_autokep_auto1_idx` (`autoID` ASC) ,
  CONSTRAINT `fk_autokep_auto1`
    FOREIGN KEY (`autoID` )
    REFERENCES `flottadb`.`auto` (`autoID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `flottadb`.`telephely`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `flottadb`.`telephely` (
  `telephelyID` INT(11) NOT NULL  AUTO_INCREMENT ,
  `telephelyNev` TEXT NULL DEFAULT NULL ,
  `telephelyCim` TEXT NULL DEFAULT NULL ,
  `telephelyTelefonszam` TEXT NULL DEFAULT NULL ,
  `telephelyXkoordinata` FLOAT NULL DEFAULT NULL ,
  `telephelyYkoordinata` FLOAT NULL DEFAULT NULL ,
  `telephelyEmail` TEXT NULL DEFAULT NULL ,
  PRIMARY KEY (`telephelyID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `flottadb`.`auto`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `flottadb`.`auto` (
  `autoID` INT(11) NOT NULL  AUTO_INCREMENT,
  `autoFoglalt` TINYINT(1) NULL DEFAULT NULL ,
  `autoXkoordinata` FLOAT NULL DEFAULT NULL ,
  `autoYkoordinata` FLOAT NULL DEFAULT NULL ,
  `autoNev` TEXT NULL DEFAULT NULL ,
  `autoTipus` TEXT NULL DEFAULT NULL ,
  `autoRendszam` TEXT NULL DEFAULT NULL ,
  `autoProfilKepID` INT(11) NULL DEFAULT NULL ,
  `autoKilometerOra` INT(11) NULL DEFAULT NULL ,
  `autoUzemAnyag` INT(11) NULL DEFAULT NULL ,
  `autoMuszakiVizsgaDate` TEXT NULL DEFAULT NULL ,
  `autoLastSzervizDate` TEXT NULL DEFAULT NULL ,
  `autoLastSoforID` INT(11) NULL DEFAULT NULL ,
  `autoLastUpDate` TEXT NULL DEFAULT NULL ,
  `autoLastTelephelyID` INT(11) NULL ,
  PRIMARY KEY (`autoID`) ,
  INDEX `AutoKep_Auto_CON` (`autoProfilKepID` ASC) ,
  INDEX `Auto_Auto_CON` (`autoLastSoforID` ASC) ,
  INDEX `fk_auto_telephely1_idx` (`autoLastTelephelyID` ASC) ,
  UNIQUE INDEX `telephely_telephelyID_UNIQUE` (`autoLastTelephelyID` ASC) ,
  CONSTRAINT `Auto_Auto_CON`
    FOREIGN KEY (`autoLastSoforID` )
    REFERENCES `flottadb`.`sofor` (`soforID` ),
  CONSTRAINT `AutoKep_Auto_CON`
    FOREIGN KEY (`autoProfilKepID` )
    REFERENCES `flottadb`.`autokep` (`autoKepID` )
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_auto_telephely1`
    FOREIGN KEY (`autoLastTelephelyID` )
    REFERENCES `flottadb`.`telephely` (`telephelyID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `flottadb`.`munkatipus`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `flottadb`.`munkatipus` (
  `munkaTipusID` INT(11) NOT NULL  AUTO_INCREMENT ,
  `munkaTipusNev` TEXT NULL DEFAULT NULL ,
  PRIMARY KEY (`munkaTipusID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `flottadb`.`partner`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `flottadb`.`partner` (
  `partnerID` INT(11) NOT NULL  AUTO_INCREMENT ,
  `partnerNev` TEXT NULL DEFAULT NULL ,
  `partnerCim` TEXT NULL DEFAULT NULL ,
  `partnerTelefonszam` TEXT NULL DEFAULT NULL ,
  `partnerXkoordinata` FLOAT NULL DEFAULT NULL ,
  `partnerYkoordinata` FLOAT NULL DEFAULT NULL ,
  `partnerWeboldal` TEXT NULL DEFAULT NULL ,
  `partnerEmailcim` TEXT NULL DEFAULT NULL ,
  PRIMARY KEY (`partnerID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `flottadb`.`munka`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `flottadb`.`munka` (
  `munkaID` INT(11) NOT NULL  AUTO_INCREMENT ,
  `munkaDate` TEXT NULL DEFAULT NULL ,
  `munkaKoltseg` INT(11) NULL DEFAULT NULL ,
  `munkaBevetel` INT(11) NULL DEFAULT NULL ,
  `munkaUzemanyagState` INT(11) NULL DEFAULT NULL ,
  `munkaComment` TEXT NULL DEFAULT NULL ,
  `munkaBefejezesDate` TEXT NULL DEFAULT NULL ,
  `munkaIsActive` TINYINT(1) NULL DEFAULT NULL ,
  `munkaEstimatedTime` INT(11) NULL DEFAULT NULL ,
  `soforID` INT(11) NULL DEFAULT NULL ,
  `munkatipusID` INT(11) NOT NULL ,
  `telephelyID` INT(11) NOT NULL ,
  `partnerID` INT(11) NOT NULL ,
  PRIMARY KEY (`munkaID`) ,
  INDEX `Munka_Sofor_CON` (`soforID` ASC) ,
  INDEX `Munka_MunkaTipus_CON` (`munkatipusID` ASC) ,
  INDEX `fk_munka_telephely1_idx` (`telephelyID` ASC) ,
  INDEX `fk_munka_partner1_idx` (`partnerID` ASC) ,
  CONSTRAINT `Munka_MunkaTipus_CON`
    FOREIGN KEY (`munkatipusID` )
    REFERENCES `flottadb`.`munkatipus` (`munkaTipusID` ),
  CONSTRAINT `Munka_Sofor_CON`
    FOREIGN KEY (`soforID` )
    REFERENCES `flottadb`.`sofor` (`soforID` ),
  CONSTRAINT `fk_munka_telephely1`
    FOREIGN KEY (`telephelyID` )
    REFERENCES `flottadb`.`telephely` (`telephelyID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_munka_partner1`
    FOREIGN KEY (`partnerID` )
    REFERENCES `flottadb`.`partner` (`partnerID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `flottadb`.`munkaeszkoz`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `flottadb`.`munkaeszkoz` (
  `munkaEszkozID` INT(11) NOT NULL  AUTO_INCREMENT ,
  `munkaEszkozNev` TEXT NULL DEFAULT NULL ,
  `munkaEszkozAr` INT(11) NULL DEFAULT NULL ,
  `munkaID` INT(11) NULL DEFAULT NULL ,
  PRIMARY KEY (`munkaEszkozID`) ,
  CONSTRAINT `Munka_MunkaEszkoz_CON`
    FOREIGN KEY (`munkaID` )
    REFERENCES `flottadb`.`munka` (`munkaID` )
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `flottadb`.`munkakep`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `flottadb`.`munkakep` (
  `munkaKepID` INT(11) NOT NULL  AUTO_INCREMENT ,
  `munkaKepPath` TEXT NULL DEFAULT NULL ,
  `munkaKepDate` TEXT NULL DEFAULT NULL ,
  `munkaID` INT(11) NULL DEFAULT NULL ,
  PRIMARY KEY (`munkaKepID`) ,
  INDEX `Munka_MunkaKep_CON` (`munkaID` ASC) ,
  CONSTRAINT `Munka_MunkaKep_CON`
    FOREIGN KEY (`munkaID` )
    REFERENCES `flottadb`.`munka` (`munkaID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `flottadb`.`partnerkepek`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `flottadb`.`partnerkepek` (
  `partnerKepID` INT(11) NOT NULL  AUTO_INCREMENT ,
  `partnerKepPath` TEXT NULL DEFAULT NULL ,
  `partnerKepDate` TEXT NULL DEFAULT NULL ,
  `partnerID` INT(11) NULL DEFAULT NULL ,
  PRIMARY KEY (`partnerKepID`) ,
  INDEX `Partner_PartnerKepek_CON` (`partnerID` ASC) ,
  CONSTRAINT `Partner_PartnerKepek_CON`
    FOREIGN KEY (`partnerID` )
    REFERENCES `flottadb`.`partner` (`partnerID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
