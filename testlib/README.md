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

## Bean Validation

Package: [guru.mikelue.msic.testlib.valiidation](src/main/java/guru/mikelue/misc/testlib/validation)

* **[AbstractSpringValidatorTestBase](src/main/java/guru/mikelue/misc/testlib/valiidation/AbstractSpringValidatorTestBase.java)**
  * [Validator](https://docs.oracle.com/javaee/7/api/javax/validation/Validator.html) - You can use *`getValidator()`* to get injected(*`@Autowired`*) instance of it.
* **[ConstraintViolationAssertions](src/main/java/guru/mikelue/misc/testlib/valiidation/ConstraintViolationAssertions.java)** -
Use this utility to construct *AssertJ* assertion from a single or a set of [ConstraintViolation](https://docs.oracle.com/javaee/7/api/javax/validation/ConstraintViolation.html)
* **[ConstraintViolationAssert](src/main/java/guru/mikelue/misc/testlib/valiidation/ConstraintViolationAssert.java)** -
Main *AssertJ* assertion for *`ConstraintViolation`*


```java
public class YourBeanTest extends AbstractSpringValidatorTestBase {
    @Test
    void validateProperty()
    {
        var violations = validateValue(YourBean.class, "name", "");

        ConstraintViolationAssertions.assertThatAsSingle(violations)
            .constraintIsTypeOfAnnotation(Size.class);
    }
}
```

----

Package: [guru.mikelue.misc.testlib.jdut](src/main/java/guru/mikelue/misc/testlib/jdut)

**[JdutYamlOverSpringJUnit5Extension](src/main/java/guru/mikelue/misc/testlib/jdut/JdutYamlOverSpringJUnit5Extension.java)**

This is a [Junit5 Extension](https://junit.org/junit5/docs/current/user-guide/#extensions) for [JDUT](https://jdut.gh.mikelue.guru/).

# Utilities

Package: [guru.mikelue.misc.testlib.assertj](src/main/java/guru/mikelue/misc/testlib/assertj)

**[AssertjUtils](src/main/java/guru/mikelue/misc/testlib/assertj/AssertjUtils.java)** - Utilities for AssertJ.
- `.asThrowingCallable(ThrowingCallableLambda)` - Adopt function to build [ThrowableAssert.ThrowingCallable](http://joel-costigliola.github.io/assertj/core-8/api/org/assertj/core/api/ThrowableAssert.ThrowingCallable.html) from a lambda expression.

See [AssertJ](https://joel-costigliola.github.io/assertj/)

# JMockit

Package: [guru.mikelue.misc.testlib.jmockit](src/main/java/guru/mikelue/misc/testlib/jdut)

**[JmockitExecutionListener](src/main/java/guru/mikelue/misc/testlib/jmockit/JmockitExecutionListener.java)**

This listener is used to integrate [JMockit](http://jmockit.github.io/) and [Spring TestContext Framework](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#testcontext-framework).

Code example(by [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)).
```java
@TestExecutionListeners(
	listeners=JmockitExecutionListener.class,
	mergeMode=MERGE_WITH_DEFAULTS
)
public class SomeTest {
	@Tested(fullyInitialized=true)
	private SampleService testedService;

	@Injectable
	private SampleDependency mockDependency;

	@Test
	void doTest()
	{
	}
}
```

----

Package: [guru.mikelue.misc.testlib.logger](src/main/java/guru/mikelue/misc/testlib/logger)

[ExchangeResultLogger](src/main/java/guru/mikelue/misc/testlib/logger/ExchangeResultLogger.java)

This logger is used to log content of [ExchangeResult](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/web/reactive/server/ExchangeResult.html).

# Planned features

- Annotation on test class/method for logging context by [SLF4j](https://www.slf4j.org).
	- Formatter
	- Logger name and level
