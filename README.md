# Solution

## Design decisions

Solution is based on technology stack

- [Java](https://www.oracle.com/java/technologies/downloads/) 17
- [Spring Boot](https://spring.io/projects/spring-boot) 3.0.0
- [Spring Data REST](https://spring.io/projects/spring-data-rest)
- [Maven](https://maven.apache.org/) build tool
- [Spring REST Docs](https://docs.spring.io/spring-restdocs/docs/3.0.x/reference/htmlsingle/) to document the APIs

### Features

I identified two separate features of the application: CRUD and Search. These two features could be implemented in separate microservices and may have
different performance requirements.

#### CRUD

For CRUD operations I used Spring Data REST. It provides implementation of all CRUD operations with pagination and sorting. Also, it implements the
HATEOAS.

#### Search

First I tried implementing Many-To-Many relations between Recipes and Ingredients to improve indexing of the latter. But the solution started growing
a lot, making it very difficult to keep it ORM compatible and Database agnostic. I made a decision that might not be optimal for search performance,
but simplifies the implementation and allows ORM and Database integrations. To optimize search I need to know the DB that is used by the company.
Options to consider are: Elastic search or Postgresql full text search.
It is also possible to write custom queries using JDBC PreparedStatement.

Query filter features:

- optional search parameters
- include or exclude items
- pagination

Query should be a GET request, thus I defined my own query parameters:

```
GET /api/recipes/search?query=key1!operation1!value1_AND_key2!operation2!value2
Where: 
'_AND_'  - is a group delimeter, groups are combined via AND operator
'!'      - is a delimeter withing group (separates key,operation,value)
allowed operations:
cn - contains
nc - doesn't contain
eq - equals
ne - doesn't equal
```

## To run

### To run from IDE

- Add a variable into Spring Boot Run configuration `Working directory: $MODULE_WORKING_DIR$`
- Run Application from your IDE: src/main/java/com/bz/challenge/ChallengeApplication.java

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

To see resources [metadata](https://docs.spring.io/spring-data/rest/docs/current/reference/html/#metadata)

```
GET /api/profile/recipes

E.G. GET http://localhost:8080/api/profile/recipes
```

## API Docs

After running tests `./mvnw clean test` the API documentation is generated in `target/generated-snippets`
The documentation can be packaged and deployed or served as
a [static content](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#web.servlet.spring-mvc.static-content)

Note: another option is to use Swagger/Open API generation of documentation. But it didn't work for me with Spring 3.0.0 (need more time to
investigate).

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
- more unit tests (not all classed are covered due to time limitation)
- integration test (integration with a specific database can be tested using Test Containers)


## Examples of REST requests
- Create
```
curl 'http://localhost:8080/api/recipes' -i -X POST \
    -H 'Accept: application/json' \
    -H 'Content-Type: application/json' \
    -d '{"name":"dish","vegetarian":false,"servingsNumber":4,"ingredients":"carrot, apple, meat","instructions":"Grind the carrots and slice the apples, add some meat"}'
```
- Retrieve
```
curl 'http://localhost:8080/api/recipes' -i -X GET \
    -H 'Accept: application/json'
```
- Update
```
curl 'http://localhost:8080/api/recipes/1' -i -X PUT \
    -H 'Accept: application/json' \
    -H 'Content-Type: application/json' \
    -d '{"name":"dish","vegetarian":false,"servingsNumber":5,"ingredients":"carrot, apple, meat","instructions":"Grind the carrots and slice the apples, add some meat"}'
```
- Delete
```
curl 'http://localhost:8080/api/recipes/1' -i -X DELETE \
    -H 'Accept: application/json'
```
- Search
```
curl 'http://localhost:8080/api/recipes/search?query=vegetarian!eq!false_AND_ingredients!cn!chicken_AND_servingsNumber!eq!4_AND_instructions!nc!cooke%20it' -i -X GET \
    -H 'Accept: application/json'
    
Response:

{
  "content": [
    {
      "name": "dish-non-veggie-4-chicken,egg",
      "vegetarian": false,
      "servingsNumber": 4,
      "ingredients": "chicken,egg",
      "instructions": "slice it, boil it"
    }
  ],
  "pageable": {
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 20,
    "unpaged": false,
    "paged": true
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 1,
  "first": true,
  "size": 20,
  "number": 0,
  "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
  },
  "numberOfElements": 1,
  "empty": false
}

```