# POC: Spring Testing

Writing unit tests to domain, persistence and web layers of a [REST API](https://en.wikipedia.org/wiki/Representational_state_transfer).

The code related to domain is implemented using [javax.validation](https://javaee.github.io/javaee-spec/javadocs/javax/validation/constraints/package-summary.html) annotations and tested using [JUnit](https://github.com/junit-team/junit5).

The code related to persistence is implemented using [Spring Data JPA](https://github.com/spring-projects/spring-data-jpa) and tested using [Mockito](https://github.com/mockito/mockito).

The code related to web (headers, status code, response body) is implemented using [Spring Framework](https://github.com/spring-projects/spring-framework) (MVC) and tested using [MockMvc](https://github.com/spring-projects/spring-framework/tree/master/spring-test).

## How to run

| Description | Command |
| :--- | :--- |
| Run tests | `./gradlew test` |
| Start the API | `./gradlew bootRun` |
