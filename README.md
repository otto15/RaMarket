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

[//]: # (Before running app change `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD`, `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` in [docker-compose.yml]&#40;https://github.com/otto15/RaMarket/blob/main/docker-compose.yml&#41; )

Before running app create `.env` file following the example below.

```bash
DB_NAME=ramarket_db
DB_PASSWORD=postgres
DB_USER=postgres
```

Now 🏃

```bash
mvn clean install
docker-compose up --build
```

### Without docker:

Be sure to create database for the service and change [application.properties](https://github.com/otto15/RaMarket/blob/main/src/main/resources/application.properties) file (put your username, password, database name instead of bold parts of the code below).

<pre>
spring.datasource.url=jdbc:postgresql://localhost:5432/<b><i>YOUR_DATABASE_NAME</i></b>?useSSL=false&amp&serverTimezone=UTC
spring.datasource.username=<b><i>USERNAME</i></b>
spring.datasource.password=<b><i>PASSWORD</i></b>
</pre>

```bash
mvn clean install
mvn spring-boot:run
```

## API Doc
The API documentation is available [here](https://github.com/otto15/RaMarket/blob/main/api-doc/openapi.yaml).
