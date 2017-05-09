Wallmart Replica
---

## Installation

- Setup the database by creating a user, password, a and database. Add your database config in `confid/database.properties`.
- Create DB tables and load the stored procedures:
 ```
 psql database_name -f /database/ddl.sql && psql database_name -f /database/StoredProcedures.sql
 ```
- Run `make` to compile and run the app.


## Testing

we'll use this temporarily:
 ```
 node javascript-tester/tester.js
 ```
