-- Roles TABLE
CREATE TABLE IF NOT EXISTS Roles  (
	ID						BIGSERIAL PRIMARY KEY,
	role_name				VARCHAR(30) NOT NULL,
	role_description		VARCHAR(200) NOT null,
	
	CONSTRAINT const_uq_Roles_role_name				UNIQUE (role_name),
	CONSTRAINT const_uq_Roles_role_description		UNIQUE (role_description)
);

--Seeding Roles Table
INSERT INTO public.roles
(id, role_name, role_description)
VALUES(nextval('roles_id_seq'::regclass), 'Super Admin', 'Super Admin is only assigned to one User and can add other types of admins, beside their permissions.');

INSERT INTO public.roles
(id, role_name, role_description)
VALUES(nextval('roles_id_seq'::regclass), 'Products Admin', 'Products Admin can add and manipulate products. This includes updating stock.');

INSERT INTO public.roles
(id, role_name, role_description)
VALUES(nextval('roles_id_seq'::regclass), 'Support Admin', 'Support Admin can claim tickets, reply to messages and can view their transactions.');

INSERT INTO public.roles
(id, role_name, role_description)
VALUES(nextval('roles_id_seq'::regclass), 'User', 'User is a customer to Walmart.');

---------------------------------------------------------------------------------------------------------------------------------------------------------
-- USERS TABLE
CREATE TABLE IF NOT EXISTS Users (
	ID						BIGSERIAL PRIMARY KEY,
	email					VARCHAR(100) NOT NULL,
	password_hash			VARCHAR(120)  NOT NULL,
	first_name				VARCHAR(30),
	last_name				VARCHAR(30),
	gender					VARCHAR(10),
	picture_path			VARCHAR(250),
	last_login				TIMESTAMP DEFAULT now(),
	last_password_change	TIMESTAMP DEFAULT now(),
	last_access				TIMESTAMP DEFAULT now(),
	disabled				BOOLEAN DEFAULT FALSE,
	reset_password			BOOLEAN DEFAULT FALSE,
	phone					VARCHAR(30),
	creation_time			TIMESTAMP DEFAULT now(),
	role_id					INT NOT NULL,

	CONSTRAINT const_uq_Users_email				UNIQUE (email),
	CONSTRAINT const_chk_Users_email			CHECK (email ~* '^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$'),
	CONSTRAINT const_chk_Users_gender			CHECK (gender in ('MALE', 'FEMALE')),
	CONSTRAINT const_fk_Users_role_id 			FOREIGN KEY(role_id) REFERENCES Roles(ID)
);
CREATE INDEX idx_Users_email ON Users USING btree(email);

--Seeding Users Table
INSERT INTO public.users
(id, email, password_hash, first_name, last_name, gender, picture_path, last_login, last_password_change, last_access, disabled, reset_password, phone, creation_time, role_id)
VALUES(nextval('users_id_seq'::regclass), 'super@example.com', '123456789', 'super', 'admin', 'MALE', '', now(), now(), now(), false, false, '01123456789', now(), 1);

---------------------------------------------------------------------------------------------------------------------------------------------------------
-- SESSIONS TABLE
CREATE TABLE IF NOT EXISTS Sessions (
	ID					BIGSERIAL PRIMARY KEY,
	user_id				INT NOT NULL,
	cookie				VARCHAR(500) NOT NULL ,
	session_start_time	TIMESTAMP DEFAULT now(),
	last_access_time	TIMESTAMP DEFAULT now(),
	address				VARCHAR(100) NOT NULL,

	CONSTRAINT const_fk_Sessions_user_id		FOREIGN KEY(user_id) REFERENCES Users(ID)
);
CREATE INDEX idx_Sessions_cookie ON Sessions USING btree(cookie);

---------------------------------------------------------------------------------------------------------------------------------------------------------
-- LOGINS TABLE
CREATE TABLE IF NOT EXISTS Logins (
	ID					BIGSERIAL PRIMARY KEY,
	user_id          	INT NOT NULL,
	last_login_time   	TIMESTAMP DEFAULT now(),
	address         	VARCHAR(100) NOT NULL,
	logout_flag      	BOOLEAN,

	CONSTRAINT const_fk_Logins_user_id FOREIGN KEY(user_id) REFERENCES Users(ID)
);
CREATE INDEX idx_Logins_user_id ON Logins USING btree(user_id);

---------------------------------------------------------------------------------------------------------------------------------------------------------
-- LOGIN FAILURES TABLE
CREATE TABLE IF NOT EXISTS LoginFailures (
	ID     	        BIGSERIAL PRIMARY KEY,
	user_id          INT NOT NULL,
	failure_time     TIMESTAMP DEFAULT now(),
	address         VARCHAR(100) NOT NULL,

	CONSTRAINT const_fk_LoginFailures_ID FOREIGN KEY(user_id) REFERENCES Users(ID)
);
CREATE INDEX idx_LoginFailures_userID ON LoginFailures USING btree(user_id);

---------------------------------------------------------------------------------------------------------------------------------------------------------
-- PRODUCTS TABLE
CREATE TABLE IF NOT EXISTS Products (
    ID  		      BIGSERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    description   text,
    price         DECIMAL(12,2),
    stock         INT NOT NULL
);
