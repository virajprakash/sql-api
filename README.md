# Java MySQL API
This library allows for making basic queries and updates to a database on the fly. 
It is extremely easy and simple to use.
# Maven
* group id: _com.virajprakash_
* artifact id: _sqlapi_
# How to use
Below are the very basics on how to use the library.
## Connect to database
```java
MySQL database = new MySQL("localhost", "3306", "database", "username", "password");
if (database.connect()) {
//Do something here
}
```
## Select from table
```java
String select = database.select(Arrays.asList("username", "password"), "Accounts", "email");
MySQLResponse response = database.executeQuery(select, "me@virajprakash.com");
```
**This would run the following query:**
```sql
SELECT username, password FROM Accounts WHERE email = "me@virajprakash.com";
```
## Insert into table
```java
String insert = database.insert("Accounts", 3);
MySQLResponse response = database.executeUpdate(insert, "me@virajprakash.com", "Viraj", "password");
```
**This would run the following query:**
```sql
INSERT INTO Accounts VALUES ("me@virajprakash.com", "Viraj", "password");
```
## Update rows in a table
```java
String update = database.update("Accounts", Arrays.asList("email", "password"), "username");
MySQLResponse response = database.executeUpdate(update, "me@virajprakash.com", "password123", "Viraj");
```
**This would run the following query:**
```sql
UPDATE Accounts SET email = "me@virajprakash.com", password = "password123" WHERE username = "Viraj";
```
## Count values in a table
```java
String count = database.count("Accounts", "Email");
MySQLResponse response = database.executeQuery(count, "me@virajprakash.com");
int result = database.getCount(response);
```
**This would run the following query:**
```sql
SELECT COUNT(*) AS count FROM Accounts WHERE Email = "me@virajprakash.com";
```
