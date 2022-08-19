# Misc - Spring Data

## Change Pageable object

In some cases, we would like to limit maximum size or page number of a page.

You can use `PageableUtils` to change page number of size of **Pageable** object.

```java
PageableUtils.LimitPageable(sourcePageable, 100, 200);
```

---

If your sorting property is differ from property name of data-mappnig-bean(e.g., ORM),
you may use `PagePropertyChanger` to convert names of propertys of `Sort`.

```java
var pageableChanger = PagePropertyChanger.from(
	Map.of("c1", "c7", "c2", "c6")
);

// This maps "c1" and "c2" to "c7" to "c6", respectively.
var modifiedPageable = testedChanger.mapProperty(
	sourcePageable
);

```

## Resolve Pageable object in method parameter of WebFlux

In additional to [HTTP query string](https://en.wikipedia.org/wiki/Query_string), the `ReactivePegeableParamResolver` and `ReactiveSortParamResolver` read paging information from [HTTP header](https://developer.mozilla.org/zh-TW/docs/Web/HTTP/Headers) either.

The paging information in HTTP header take precedence over than query string.

Differ from [spring-data](https://docs.spring.io/spring-data/data-jpa/docs/current/reference/html/#core.web.basic), these implementation use following header name/parameter name by default.

* `page` - The number of page
* `page-size` - The size of page
* `page-sort` - The sorting syntax of page

As for `page-sort`, the syntax as:

```bnf
<props> ::= <prop> | <prop> ',' <props>
<prop> ::= <prop-name> | <prop-name> ':' <direction>
<direction> ::= 'asc' | 'desc'
```

e.g. `name:asc,phone,address`

For HTTP specification, client can put paging information to either *header* or *query string*.

```sh
curl http://your-api/api?page=2&page-size=40&page-sort=name,size:desc
curl -H "page: 2" -H "page-size: 40" -H "page-sort: name,size:desc" http://your-api/api
```

Usage:

```java

import guru.mikelue.springframework.data.web.ReactivePageableParamResolver;
import guru.mikelue.springframework.data.web.ReactiveSortParamResolver;

@Configuration(proxyBeanMethods=false)
public class WebConfig extends WebFluxConfigurationSupport {
	@Override
	protected void configureArgumentResolvers(ArgumentResolverConfigurer configurer)
	{
		var sortResolver = new ReactiveSortParamResolver();
		var pageableResolver = new ReactivePageableParamResolver();
		pageableResolver.setSortResolver(sortResolver);

		configurer.addCustomResolver(sortResolver);
		configurer.addCustomResolver(pageableResolver);
	}
}
```
