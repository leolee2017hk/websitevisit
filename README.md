# websitevisit

* The project is implemented using java and MySQL
* Java 8 SDK is required
* Particularly, Springboot is used as a self-contained application

## Prerequisite

* Java 8 SDK
* MySQL 5.x
* Maven 3.5
* Git 2.13

## Set Up MySQL DB
Create a MySQL DB <br>
instance name: webvisit <br>
schema name: webvisit <br>

Apply the following DDL

https://github.com/leolee2017hk/websitevisit/blob/master/db/create_table.sql

## application.properties

## Checkout Source Code

*git clone https://github.com/leolee2017hk/websitevisit.git*

## Run Unit Test

*mvn clean test*

## Run Integration Test

*mvn clean test -Dtest=WebControllerIT*

** Build and Packaging

*mvn clean package*

A new file top5website-0.0.1-SNAPSHOT.war is generated under target directory

## Deployment

Copy top5website-0.0.1-SNAPSHOT.war to the host

## Run

*java -jar top5website-0.0.1-SNAPSHOT.war*

## Access the website vis AWS Hosting

http://ec2-52-221-231-45.ap-southeast-1.compute.amazonaws.com:8080/webvisit/

*Login:*<br>
username: webuser<br>
password: Abcd1234<br>

## To Do

* The source code will be packaged as a WAR file and the WAF file will be deployed to Tomcat container
* The user credentials will be stored in the database
* The DB credentials will be encrypted in the application.properties and decrypted during runtime by a secret key

