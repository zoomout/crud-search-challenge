# Solution

## Design decisions

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

## To test

```
./mvnw clean test
```

## Service discoverability

[Resource discoverability](https://docs.spring.io/spring-data/rest/docs/current/reference/html/#repository-resources.resource-discoverability)

Make HTTP call
```
GET http://localhost:8080/api/
```
Response
```
{
    "_links": {
        "recipe": {
            "href": "http://localhost:8080/api/recipes"
        },
        "profile": {
            "href": "http://localhost:8080/api/profile"
        }
    }
}
```

## API Docs

After running tests `./mvnw clean test` the API documentation is generated in `target/generated-snippets`
The documentation can be packaged and deployed or served as
a [static content](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#web.servlet.spring-mvc.static-content)

## To run

### To run from IDE

Add a variable into Spring Boot Run configuration `Working directory: $MODULE_WORKING_DIR$`

### To run as a JAR

Build the jar and run it (providing production.yml with configuration)

```
java -jar {DIR}/challenge-0.0.1-SNAPSHOT.jar --spring.config.additional-location=production.yml
```

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
- more unit tests