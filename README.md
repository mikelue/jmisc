This repository contains **not-matured, experimental libraries** used in my work.

## Summary

[**lang**](lang/README.md): Tuple, Data Generator(for testing, mostly)
```xml
<dependency>
	<groupId>guru.mikelue.misc</groupId>
	<artifactId>lang</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```

[**hibernate-datatype**](hibernate-datatype/README.md): Data types of hibernate supporting for specific database(Postgres)
```xml
<dependency>
	<groupId>guru.mikelue.misc</groupId>
	<artifactId>hibernate-datatype</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```

[**testlib**](testlib/README.md): AssertJ, JUnit 5 enhancements
```xml
<dependency>
	<groupId>guru.mikelue.misc</groupId>
	<artifactId>testlib</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```

## Usage

This project won't be push to Maven repository for now, you should use [git submodule](https://git-scm.com/docs/git-submodule) to include the code in your repository or your CI-building.

```bash
mvn -P local-install clean install
```
The profile of `local-install` will skip all of the tests for this project.

## Dependencies

`testlib` could be used by any other libraries in [test scope](http://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html#Dependency_Scope).
