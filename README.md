## Solution

### Design decisions

Solution is based on technology stack 
- [Java](https://www.oracle.com/java/technologies/downloads/) 17 
- [Spring Boot](https://spring.io/projects/spring-boot) 3.0.0
- [Spring Data REST](https://spring.io/projects/spring-data-rest)
- [Maven](https://maven.apache.org/) build tool
- [Spring REST Docs](https://docs.spring.io/spring-restdocs/docs/3.0.x/reference/htmlsingle/) to document the APIs

Query filter features:

- optional search parameters
- include or exclude items
- pagination

## To build
```
./mvnw clean install
```

# To test

## To run unit tests

```
./mvnw clean test
```

# To make it production ready

- add logging/configure logging appender
- use real DB datasource, tune DB connection pool
- add caching
- service discovery
- authentication
- circuit breaker
- metrics
- add tracing
- more unit tests

## To run

## To run from IDE
Add a variable into Spring Boot Run configuration `Working directory: $MODULE_WORKING_DIR$`

## To run as a JAR
Build the jar and run it (providing production.yml with configuration)
```
java -jar {DIR}/challenge-0.0.1-SNAPSHOT.jar --spring.config.additional-location=production.yml
```