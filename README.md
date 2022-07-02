# RaMarket
Task for Yandex School of Backend

## Overview
REST API Wev-Service for price comparison. Designed with JAVA (Spring Boot) and PostgreSQL.

## Description
Service allows to work with `ShopUnit` entity: create, update, retrieve and get statistic.

## Install & Run

### Clone repository and install the project:

```bash
git clone https://github.com/otto15/RaMarket.git
```

### Run service using docker:

Before running app change `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD`, `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` in [docker-compose.yml](https://github.com/otto15/RaMarket/blob/main/docker-compose.yml) 

```bash
mvn clean install
docker-compose up --build
```

### Without docker:

Be sure to create database for the service and change [application.properties](https://github.com/otto15/RaMarket/blob/main/src/main/resources/application.properties) file (put your username, password, database name instead of bold parts of the code below).

<pre>
spring.datasource.url=jdbc:postgresql://localhost:5432/<b>YOUR_DATABASE_NAME</b>?useSSL=false&amp&serverTimezone=UTC
spring.datasource.username=<b>USERNAME</b>
spring.datasource.password=<b>PASSWORD</b>
</pre>

```bash
mvn clean install
mvn spring-boot:run
```

## API Doc
The API documentation is available [here](https://github.com/otto15/RaMarket/blob/main/api-doc/openapi.yaml).
