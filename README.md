# Expense Tracker REST API

## 1. About Expense Tracker REST API
This API uses PostgreSQL database for storing users information and provides ability to use all basic CRUD operations on users.

## 2. How to run this API
Before running this API, I assume that you have successfully installed the following tools on your computer:
- Git - for cloning repository from GitHub;
- Maven - for building project;
- JDK - required for Maven compilation;
- Docker - for deploying application;
- Docker-compose - for running multi-container Docker applications;
- API client tool (e.g. Postman) or browser - for testing your API.

`Step 1` - build docker image:

Navigate to root of the project and execute command:

    $ mvn spring-boot:build-image

This command will also run unit tests.

`Step 2` - start your project`s services:

    $ docker-compose up -d

This will start PostgreSQL and application services in the background.

## 3. Technologies and Frameworks

- Spring Boot;
- Spring Data Rest;
- PostgreSQL;
- Docker-compose;
- Mockito;
- JUnit;
- Flyway;
- Testcontainers.