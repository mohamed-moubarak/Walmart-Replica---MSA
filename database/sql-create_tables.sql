
-- CREATE EXTENSION pgcrypto; 
-- you need that for encrypting password. Run it once only to install in PostgreSQL engine.


CREATE SEQUENCE seq_Users_ID  MINVALUE 1 START WITH 1;
CREATE TABLE IF NOT EXISTS Users (
	ID                     		INT NOT NULL DEFAULT NEXTVAL('seq_Users_ID'),
	email                  		VARCHAR(100) NOT NULL,
	passwordHash               	VARCHAR(120)  NOT NULL,
	oldPassword       		    VARCHAR(30),
	firstName              		VARCHAR(30),
	lastName               		VARCHAR(30),
    gender                      VARCHAR(10), 
    picturePath                 VARCHAR(250),
    lastLogin              		TIMESTAMP,
	lastPasswordChange     		TIMESTAMP,
	lastAccess             		TIMESTAMP,
	disabled               		BOOLEAN DEFAULT FALSE,
	resetPassword          		BOOLEAN DEFAULT FALSE,
	question          		    VARCHAR(200),
	answer            		    VARCHAR(200), 
	phone 		        		VARCHAR(30),
    verified                    BOOLEAN,
    verificationCode            VARCHAR(5),
    creationTime    		    TIMESTAMP NOT NULL,
    CONSTRAINT const_pk_Users_ID                		PRIMARY KEY (ID),
    CONSTRAINT const_uq_Users_email             		UNIQUE (email),       	
	CONSTRAINT const_chk_Users_email  CHECK (email ~* '^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$'),
	CONSTRAINT const_chk_Users_gender CHECK (gender in ('MALE', 'FEMALE') )
);
CREATE INDEX idx_Users_email ON Users USING btree(email);


CREATE SEQUENCE seq_Sessions_ID  MINVALUE 3249 START WITH 3249;
CREATE TABLE IF NOT EXISTS Sessions (
    ID                  BIGINT NOT NULL DEFAULT NEXTVAL('seq_Sessions_ID'),
    userID              INT NOT NULL,
    cookie              VARCHAR(500) NOT NULL ,
    sessionStartTime    TIMESTAMP NOT NULL,
    lastAccessTime      TIMESTAMP NOT NULL,
	address     		VARCHAR(100) NOT NULL,
    CONSTRAINT const_pk_Sessions_sessionID  PRIMARY KEY(ID),
    CONSTRAINT const_fk_Sessions_userID  	FOREIGN KEY(userID) REFERENCES Users(ID)
);
CREATE INDEX idx_Sessions_cookie ON Sessions USING btree(cookie);

CREATE SEQUENCE seq_Logins_ID  MINVALUE 2343 START WITH 2343;
CREATE TABLE IF NOT EXISTS Logins (
    ID     	        BIGINT NOT NULL DEFAULT NEXTVAL('seq_Logins_ID'), 
    userID          INT NOT NULL,
    lastLoginTime   TIMESTAMP NOT NULL,
    address         VARCHAR(100) NOT NULL,
    logoutFlag      BOOLEAN,
	CONSTRAINT const_uq_Logins_ID UNIQUE (ID),    
    CONSTRAINT const_fk_Logins_userID FOREIGN KEY(userID) REFERENCES Users(ID) 	
);
CREATE INDEX idx_Logins_userID ON Logins USING btree(userID);  
 
CREATE SEQUENCE seq_LoginFailures_ID  MINVALUE 1 START WITH 1;
CREATE TABLE IF NOT EXISTS LoginFailures (
	ID     	        INT NOT NULL DEFAULT NEXTVAL('seq_LoginFailures_ID'), 
    userID          INT NOT NULL,    
    failureTime     TIMESTAMP NOT NULL,
    address         VARCHAR(100) NOT NULL,
	CONSTRAINT const_fk_LoginFailures_ID FOREIGN KEY(userID) REFERENCES Users(ID) 
);
CREATE INDEX idx_LoginFailures_userID ON LoginFailures USING btree(userID); 
