This library contains language enhancements for Java.

## Summary(by package)

*guru.mikelue.misc.lang.agent*:

Defines interfaces to represent a object as a value

- **StringAgent** - An object as a string
- **IntegerAgent** - An object as a int

In case of database constant(enumerable data), the code in Java must be careful to match the existing data in database.

For example:
```java
public enum BigCat {
	Tiger, Lion, Jaguar
}
```

If the value in database depends on [Enum.ordinal()](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Enum.html#ordinal()),
the state of database **would be puzzled** by changing the sequence of enum instances.

As a safety, you should write the plain text as value of database:
```java
public enum BigCat implements IntegerAgent {
	Tiger(1), Lion(2), Jaguar(3)

	private final int dbValue;
	BigCat(int dbValue)
	{
		this.dbValue = dbValue;
	}

	@Override
	public Integer value()
	{
		return this.dbValue;
	}
}
```

---

*guru.mikelue.misc.lang.data*:
- **BagDataFactory** - Out of box utilities to generate a bag of data provided by [Supplier](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/Supplier.html).
	- Supported types: [array](https://www.w3schools.com/java/java_arrays.asp), [List](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/List.html), [Set](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Set.html), [Map](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Map.html), [Stream](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/Stream.html)
- **BagDataBuilder** - Likes `BagDataFactory`, but it is defined as a reusable instance.
- **CartesianProduct** - Data generator by [Cartesian product](https://en.wikipedia.org/wiki/Cartesian_product) theory.

---

*guru.mikelue.misc.lang.tuple*:

Likes [javatuples](https://www.javatuples.org/index.html) or [Reactor Tuples](https://projectreactor.io/docs/core/release/api/index.html?reactor/util/function/Tuples.html), but this implementation are immuable and simpler than javatuples.

- **Tuple** - Main class to construct tuple object
