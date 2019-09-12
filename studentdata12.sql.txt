DROP DATABASE IF EXISTS studentdata12;
CREATE DATABASE studentdata12;

USE studentdata12;

CREATE TABLE student (
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(30),
  last_name VARCHAR(30),
  program_name VARCHAR(30),
  program_year INT UNSIGNED,
  program_coop BOOLEAN,
  program_internship BOOLEAN,
  
  PRIMARY KEY(id) 
);

CREATE TABLE users (
	user_login VARCHAR(15) NOT NULL,
    user_password VARCHAR(128) NOT NULL,
    
	PRIMARY KEY(user_login)
);

CREATE TABLE roles (
	user_login VARCHAR(15) NOT NULL,
	user_role VARCHAR(15) NOT NULL,
    
    PRIMARY KEY (user_login, user_role),
    FOREIGN KEY(user_login)
		REFERENCES users(user_login)
);

CREATE TABLE persistent_logins ( 
  username varchar(100) not null, 
  series varchar(64) primary key, 
  token varchar(64) not null, 
  last_used timestamp not null
);

INSERT INTO student 
	(first_name, last_name, program_name, program_year, program_coop, program_internship)
VALUES
	('Harry','Potter','Computer Programmer',1,true,false),
	('Ronald','Weasley','Systems Technician',2,false,true),
	('Hermione','Granger','Systems Technology',1,false,false),
    ('Draco','Malfoy','Engineering Technician',2,true,true);

/* all these passwords are "sesame" */
INSERT INTO users 
	(user_login, user_password)
	VALUES
    ('marge','$2a$10$bxGtVIu12/dXFQ8I1VrCmeFap8AXK.8EFgp.NRgaGt5no27uZd8Ty'),
    ('homer','$2a$10$5y39gonhJWNtUXFHi3gLaumMYLKmK/O4Jshi4/IlhryYNxhEFSNuy'),
    ('bart','$2a$10$WFceIBbBe2ynUC6ckJltOeI9qNgKSqGzE/PqD2BbxBHSVZyscOF8O'),
    ('lisa','$2a$10$/0le0donOsBt.kSva6CNNeNXRjm83m.VQeEsWHyY9ORQwJeGN/DAa');
    
    
INSERT INTO roles
	(user_login, user_role)
    VALUES
	('marge','ROLE_ADMIN'),
    ('homer','ROLE_ADMIN'),
    ('bart','ROLE_USER'),
    ('lisa','ROLE_USER');

    
DELIMITER //
CREATE PROCEDURE drop_user_if_exists(IN user_name VARCHAR(20))
BEGIN
    DECLARE userCount BIGINT DEFAULT 0 ;
    SELECT COUNT(*) INTO userCount FROM mysql.user
    WHERE User = user_name and  Host = 'localhost';
    IF userCount > 0 THEN
		SET @SQL_STATEMENT = CONCAT('DROP USER ',user_name,'@localhost');
		PREPARE PREP_S FROM @SQL_STATEMENT;
        EXECUTE PREP_S;
        DEALLOCATE PREPARE PREP_S;
    END IF;
END; //
DELIMITER ;

CALL drop_user_if_exists('sd12_user') ;        
CREATE USER sd12_user@localhost IDENTIFIED BY 'sesame';

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP
ON studentdata12.*
TO sd12_user@localhost;

