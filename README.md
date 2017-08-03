# websitevisit

* The project is implemented using java and MySQL
* Java 8 SDK is required
* Particularly, Springboot is used as a self-contained application

## Prerequisite

* Java 8 SDK
* MySQL 5.x
* Maven 3.5
* Git 2.13
* Chrome browser 

## Set Up MySQL DB
Create a MySQL DB <br>
instance name: webvisit <br>
schema name: webvisit <br>

Apply the following DDL

https://github.com/leolee2017hk/websitevisit/blob/master/db/create_table.sql

## application.properties
System and application configuration is set up on 
https://github.com/leolee2017hk/websitevisit/blob/master/src/main/resources/application.properties

The following should be configurated specifically

* jdbc.url=<jdbc_connection_strong>
* jdbc.username=<db_username>
* jdbc.password=<db_password>

* source.path=<path_of_source_data_file>
* query.top.N=<number_of_top_records_returned>
* exclusion.url=<endpoint_of_exclusion_list_API>
* web.username=<web_login_usename>
* web.password=<web_login_password>

## Checkout Source Code

*git clone https://github.com/leolee2017hk/websitevisit.git*

After<br>
*cd websitevisit*<br>
Then you are in the root directory of the repo

## Run Unit Test

*mvn clean test*

## Run Integration Test

* [__Selenium__](http://www.seleniumhq.org/) is used to for integration test to simulate the behaviour in the browser
* Chrome driver has been download and checked in to https://github.com/leolee2017hk/websitevisit/blob/master/bin/chromedriver.exe

*mvn clean test -Dtest=WebControllerIT*

## Build and Packaging

*mvn clean package*

A new file top5website-0.0.1-SNAPSHOT.war is generated under **target** directory

## Deployment

Copy top5website-0.0.1-SNAPSHOT.war to the host

## Run

*java -jar top5website-0.0.1-SNAPSHOT.war*

## Access the website vis AWS Hosting

http://ec2-52-221-231-45.ap-southeast-1.compute.amazonaws.com:8080/webvisit/

*Login:*<br>
username: webuser<br>
password: Abcd1234<br>

To make it run on AWS, we need to replace the content of [application.properties](https://github.com/leolee2017hk/websitevisit/blob/master/src/main/resources/application.properties) from [application.properties.aws](https://github.com/leolee2017hk/websitevisit/blob/master/src/main/resources/application.properties.aws)  under 
[resources](https://github.com/leolee2017hk/websitevisit/tree/master/src/main/resources) before we build and package it as a __war__ file

## To Do

* The source code will be packaged as a WAR file and the WAF file will be deployed to Tomcat container
* The user credentials will be stored in the database
* The DB credentials will be encrypted in the application.properties and decrypted during runtime by a secret key

