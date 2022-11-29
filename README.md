# Solution

## Design decisions

Solution is based on technology stack

- [Java](https://www.oracle.com/java/technologies/downloads/) 17
- [Spring Boot](https://spring.io/projects/spring-boot) 3.0.0
- [Spring Data REST](https://spring.io/projects/spring-data-rest)
- [Maven](https://maven.apache.org/) build tool
- [Spring REST Docs](https://docs.spring.io/spring-restdocs/docs/3.0.x/reference/htmlsingle/) to document the APIs

### Features

I identified two separate features of the application: CRUD and Search. These two features could be even implemented in separate microservices and
have different performance requirements.

#### CRUD

For CRUD operations Spring Data REST is used. It provides implementation of all CRUD operations with pagination and sorting. Also, it implements the
HATEOAS.

#### Search

First I tried implementing Many-To-Many relations between Recipes and Ingredients to improve indexing of the latter.
But the solution started growing a lot, making it very difficult to keep it ORM compatible and Database agnostic.
I made a decision that might not be optimal for search performance, but simplifies the implementation and allows ORM and Database integrations.
To optimize search I need to know the DB that is used by the company. 
Options to consider are Elastic search or Postgresql full text search.

Query filter features:

- optional search parameters
- include or exclude items
- pagination

## To run

### To run from IDE

Add a variable into Spring Boot Run configuration `Working directory: $MODULE_WORKING_DIR$`

### To run as a JAR

Build the jar and run it

```
./mvnw clean package
java -jar ./target/challenge-0.0.1-SNAPSHOT.jar --spring.config.additional-location=application.yml
```

## To test

```
./mvnw clean test
```

## Service discoverability

[Resource discoverability](https://docs.spring.io/spring-data/rest/docs/current/reference/html/#repository-resources.resource-discoverability)

To see resources [metadata]{https://docs.spring.io/spring-data/rest/docs/current/reference/html/#metadata}

```
GET /api/profile/recipes

E.G. GET http://localhost:8080/api/profile/recipes
```

## API Docs

After running tests `./mvnw clean test` the API documentation is generated in `target/generated-snippets`
The documentation can be packaged and deployed or served as
a [static content](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#web.servlet.spring-mvc.static-content)

Note: another option is to use Swagger/Open API generation of documentation. But it didn't work for me with Spring 3.0.0 (need more time to investigate).

## What is left to make it production ready

- improve documentation
- add logging/configure logging appender
- use real DB datasource, tune DB connection pool
- add caching
- service discovery
- security/authentication
- circuit breaker
- metrics
- add tracing
- more tests
- integration test (integration with a specific database can be tested using Test Containers)
