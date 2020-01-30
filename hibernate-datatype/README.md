This library provides [data type](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#basic-custom-type) for specific database.

## Postgres

The [PostgreSql](https://www.postgresql.org/) has [Enumerated Type](https://www.postgresql.org/docs/current/datatype-enum.html), [Arrays](https://www.postgresql.org/docs/current/arrays.html), and [JSON](https://www.postgresql.org/docs/current/datatype-json.html) types,
which needs customized User Type for mapping of [Hibernate ORM](https://hibernate.org/orm/).

Pacakges: *guru.mikelue.misc.hibernate.postgres.*

- Mapping of arrays: **PostgresArrayUserType**
- Mapping of enumerated types: **PostgresEnumUserType**
- Mapping of JSON: **PostgresJsonUserType**

### JSON

Put following property in `hiberante.properties`:

```properties
hibernate.jackson.object_mapper_supplier=<your class of supplier>
```

The value of `hibernate.jackson.object_mapper_supplier` must be the type of [Supplier<ObjectMapper>](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/Supplier.html).

## Planned features

- It is reasonable to use [ANTLR](https://www.antlr.org/) to parse [Composite Types](https://www.postgresql.org/docs/current/rowtypes.html) of PostgtreSql.
- Supported [Range types](https://www.postgresql.org/docs/current/rangetypes.html).
