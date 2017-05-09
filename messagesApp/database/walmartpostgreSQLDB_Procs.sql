#----------------------------------------------------------------------------#
# ADD STOCK
CREATE OR REPLACE FUNCTION addStock
( pStock INT )
RETURNS INT as '
  DECLARE pID int;
  BEGIN
  SELECT INTO pID nextval(''seq_Products_ID'');
  INSERT into products (id,stock)
  VALUES ( pID, pStock );
  RETURN pID;
END;'
language 'plpgsql';
#----------------------------------------------------------------------------#
# GET STOCK
create or replace function getStock
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
#----------------------------------------------------------------------------#
# UPDATE STOCK
CREATE OR REPLACE FUNCTION updateStock
( pID INT, newStock INT )
RETURNS INT as'
BEGIN
    UPDATE Products
    SET Stock = newStock
    WHERE ID = pID;
    RETURN pID;
END;'
language 'plpgsql';
#----------------------------------------------------------------------------#
# DECREASE STOCK
CREATE OR REPLACE FUNCTION decreaseStock
( pID INT )
RETURNS INT as'
BEGIN
  UPDATE Products
  SET Stock = Stock - 1
  WHERE ID = pID;
  RETURN pID;
END;'
language 'plpgsql';
#----------------------------------------------------------------------------#
CREATE OR REPLACE FUNCTION editinfo
(
    pUserEmail      VARCHAR(30),
    pPassword   VARCHAR(30),
    pNewPassword    VARCHAR(30),
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
          SET passwordHash = lPasswordHash
          WHERE ID = lUserID;
        end if;
        if (pFirstName is not null) then
          UPDATE users
          SET firstName  = pFirstName
          WHERE ID = lUserID;
        end if;
        if (pLastName is not null) then
          UPDATE users
          SET lastName = pLastName
          WHERE ID = lUserID;
        end if;
        if (pPicturePath is not null) then
          UPDATE users
          SET picturePath = pPicturePath
          WHERE ID = lUserID;
        end if;
        if (pgender is not null) then
          UPDATE users
          SET gender =  pgender
          WHERE ID = lUserID;
        end if;
    endif;
    RETURN 0;
END;'
language 'plpgsql';
#----------------------------------------------------------------------------#
