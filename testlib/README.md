This library provides enhancements over testing libraries.

## [AssertJ](https://joel-costigliola.github.io/assertj/)

- **AssertjUtils** - Utilities for AssertJ.
	- `.asThrowingCallable(ThrowingCallableLambda)` - Adopt function to build [ThrowableAssert.ThrowingCallable](http://joel-costigliola.github.io/assertj/core-8/api/org/assertj/core/api/ThrowableAssert.ThrowingCallable.html) from a lambda expression.

## Planned features

- Annotation on test class/method for logging context by [SLF4j](https://www.slf4j.org).
	- Formatter
	- Logger name and level
