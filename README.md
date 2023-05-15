# Object Oriented Programming Project - Bangs & Bucks

## General Description
Bangs & Bucks is a CLI banking system simulator, with database persistency and
a powerful auditing feature. In terms of entities, several real-life banking
concepts were emulated, along with their corresponding actions: 
- Individuals and Companies as banking entities
- Currencies -> dollar conversion factor provided, as to perform exchanges 
between various currencies
- Simple and Savings Accounts -> deposits, withdrawals, transfers, saving 
interest rates
- Term deposits -> emptying allowed only after the agreed upon period had 
passed
- Cards -> associated with a single account
- Transactions -> in-memory storage of all transactions associated with a 
given session

## Dependencies
The project was written according to the Java 17 specification and no guarantees
can be made about future or backwards compatibility. The JDBC API was used as the
database interface and all the provided queries are written in the MYSQL/MariaDB
dialect.

* MariaDB JDBC driver v3.1.4
