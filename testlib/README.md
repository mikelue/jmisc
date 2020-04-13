This library provides enhancements over testing libraries.

# Base classes for testing

## Common

Package: `guru.mikelue.misc.testlib`

* [AbstractTestBase](src/main/java/guru/mikelue/misc/testlib/AbstractTestBase.java) - Built-in with:
	* [Logger](http://www.slf4j.org/apidocs/org/slf4j/Logger.html)

All of the following classes for testing are inheriting *`AbstractTestBase`*.

This base applies `@ExtendWith(ExceptionLoggerExtension` by default.

## JPA

Package: [guru.mikelue.misc.testlib.jpa](src/main/java/guru/mikelue/misc/testlib/jpa)

* [AbstractJpaReposTestBase](src/main/java/guru/mikelue/misc/testlib/jpa/AbstractJpaReposTestBase.java) - Injected(*[@Autowired](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/beans/factory/annotation/Autowired.html)*) with:
	* [EntityManager](https://javaee.github.io/javaee-spec/javadocs/javax/persistence/EntityManager.html)
	* [DataSource](https://docs.oracle.com/en/java/javase/13/docs/api/java.sql/javax/sql/DataSource.html)
* [SpringBootJpaReposTestBase](src/main/java/guru/mikelue/misc/testlib/jpa/SpringBootJpaReposTestBase.java)(*extends `AbstractJpaReposTestBase`*) - Annotated with:
	* [@DataJpaTest](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/orm/jpa/DataJpaTest.html)
	* [@AutoConfigureTestDatabase(replace=Replace.NONE)](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/jdbc/AutoConfigureTestDatabase.html)

## Web

Package: [guru.mikelue.misc.testlib.web](src/main/java/guru/mikelue/misc/testlib/web)
* [AbstractSpringWebTestBase](src/main/java/guru/mikelue/misc/testlib/web/AbstractSpringWebTestBase.java) - Built-in with:
	* [ExchangeResultLogger](src/main/java/guru/mikelue/misc/testlib/logger/ExchangeResultLogger.java)
* [SpringWebClientTestBase](src/main/java/guru/mikelue/misc/testlib/web/SpringWebClientTestBase.java)(*extends `AbstractSpringWebTestBase`*) - Injected(*`@Autowired`*) with:
	* [WebTestClient](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/web/reactive/server/WebTestClient.html)

This group of base classes supports *ExchangeResultLogger*.

## JUnit5 extensions

Package: [guru.mikelue.misc.testlib.junit](src/main/java/guru/mikelue/misc/testlib/junit)

**[ExceptionLoggerExtension](src/main/java/guru/mikelue/misc/testlib/junit/ExceptionLoggerExtension.java)**

This extentions outputs stack trace while there is exception raised from tested code.

----

Package: [guru.mikelue.misc.testlib.jdut](src/main/java/guru/mikelue/misc/testlib/jdut)

**[JdutYamlOverSpringJUnit5Extension](src/main/java/guru/mikelue/misc/testlib/jdut/JdutYamlOverSpringJUnit5Extension.java)**

This is a [Junit5 Extension](https://junit.org/junit5/docs/current/user-guide/#extensions) for [JDUT](https://jdut.gh.mikelue.guru/).

# Utilities

Package: [guru.mikelue.misc.testlib.assertj](src/main/java/guru/mikelue/misc/testlib/assertj)

**[AssertjUtils](src/main/java/guru/mikelue/misc/testlib/assertj/AssertjUtils.java)** - Utilities for AssertJ.
- `.asThrowingCallable(ThrowingCallableLambda)` - Adopt function to build [ThrowableAssert.ThrowingCallable](http://joel-costigliola.github.io/assertj/core-8/api/org/assertj/core/api/ThrowableAssert.ThrowingCallable.html) from a lambda expression.

See [AssertJ](https://joel-costigliola.github.io/assertj/)

----

Package: [guru.mikelue.misc.testlib.logger](src/main/java/guru/mikelue/misc/testlib/logger)

[ExchangeResultLogger](src/main/java/guru/mikelue/misc/testlib/logger/ExchangeResultLogger.java)

This logger is used to log content of [ExchangeResult](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/web/reactive/server/ExchangeResult.html).

# Planned features

- Annotation on test class/method for logging context by [SLF4j](https://www.slf4j.org).
	- Formatter
	- Logger name and level
