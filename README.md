Steps involved to import and run the application:
1) Import the spring boot project into any IDE eg: IntelliJ or eclipse
2) Build the project using maven by doing clean install
3) For database, I have used mysql to save and fetch the transaction records
4) The database details are mentioned in application.properties file
5) username=**root**, password=**admin@123**, url=**jdbc:mysql://localhost:3306/world**
6) world is database name and table name is **digital_wallet**
7) Table creation command:
   create table world.digital_wallet(
   id int(11) NOT NULL AUTO_INCREMENT,
   amount DOUBLE NOT NULL,
   balance DOUBLE NOT NULL,
   credit_date DATETIME NOT NULL,
   PRIMARY KEY (id)
   );
8) Once db setup is done, got to BitcoinApplication.java and run the main method which would boot up the application.
9) You will see **Started BitcoinApplication** in the console logs
10) To insert record, make post call to endpoint
    http://localhost:8080/creditwallet
    json payload:
    {
    "dateTime": "2019-10-05T19:50:05",
    "amount": 10.0
    }
11) To fetch record based on given time, make get call to endpoint
    http://localhost:8080/walletbalance?start=2019-10-05T14:45:00&end=2019-10-05T22:45:05
    Here start and end are request parameters with type as datetime