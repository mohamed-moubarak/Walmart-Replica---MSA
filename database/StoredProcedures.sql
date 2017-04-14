CREATE OR REPLACE FUNCTION addProduct
(
  pName VARCHAR(100),
  pDescription text,
  pPrice DECIMAL(12,2),
  pStock INT
)
RETURNS INT as '
  DECLARE pID int;
  BEGIN
  SELECT INTO pID nextval(''products_id_seq'');
  INSERT into products (ID, name, description, price, stock)
  VALUES ( pID, pName, pDescription, pPrice, pStock );
  RETURN pID;
END;'
language 'plpgsql';

CREATE OR REPLACE FUNCTION getStock
( pID  INT )
RETURNS INT as'
DECLARE lStock INT;
BEGIN
  SELECT into lStock stock
  FROM Products
  WHERE ID = pID;
  RETURN lStock;
end;'
language 'plpgsql';

CREATE OR REPLACE FUNCTION updateStock
( pID INT, newStock INT )
RETURNS INT as'
BEGIN
    UPDATE Products
    SET stock = newStock
    WHERE ID = pID;
    RETURN pID;
END;'
language 'plpgsql';

CREATE OR REPLACE FUNCTION decreaseStock
( pID INT )
RETURNS INT as'
BEGIN
  UPDATE Products
  SET stock = stock - 1
  WHERE ID = pID;
  RETURN pID;
END;'
language 'plpgsql';

CREATE OR REPLACE FUNCTION verifyPassword
( uEmail VARCHAR(100), uPassword VARCHAR(120) )
RETURNS INT as'
declare
    oldPassword     VARCHAR(120);
BEGIN
  SELECT INTO oldPassword password_hash from users WHERE email = uEmail;
  if ( oldPassword = uPassword ) then
    RETURN 0;
  end if;
  RETURN -1;
END;'
language 'plpgsql';

CREATE OR REPLACE FUNCTION editinfo
(
    pUserEmail      VARCHAR(100),
    pPassword   VARCHAR(120),
    pNewPassword    VARCHAR(120),
    pFirstName  VARCHAR(30),
    pLastName       VARCHAR(30),
    pPicturePath     VARCHAR(250),
    pgender          VARCHAR(10)
)
returns INT as'
declare
    lPasswordHash   VARCHAR(120);
    lPicturePath    VARCHAR(250);
    lUserID         INT;
    lResult         INT;
BEGIN

    SELECT into lResult verifyPassword( pUserEmail, pPassword );

    if( lResult = 0 ) then
        SELECT into lUserID  ID from Users where email = pUserEmail;

        if( pPassword is not null) then
          SELECT into lPasswordHash crypt( pNewPassword, gen_salt(''bf''));
          UPDATE users
          SET password_hash = lPasswordHash
          WHERE ID = lUserID;
        	end if;
        if (pFirstName is not null) then
          UPDATE users
          SET first_name  = pFirstName
          WHERE ID = lUserID;
       		end if;
        if (pLastName is not null) then
          UPDATE users
          SET last_name = pLastName
          WHERE ID = lUserID;
        	end if;
        if (pPicturePath is not null) then
          UPDATE users
          SET picture_path = pPicturePath
          WHERE ID = lUserID;
        	end if;
        if (pgender is not null) then
          UPDATE users
          SET gender =  pgender
          WHERE ID = lUserID;
        	end if;
    end if;
    RETURN 0;
END;'
language 'plpgsql';

