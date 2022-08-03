package guru.mikelue.springframework.data.web;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.JsonTestersAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContentAssert;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import reactor.core.publisher.Mono;

import static guru.mikelue.springframework.data.web.ReactivePageableParamResolver.*;
import static org.assertj.core.api.Assertions.*;

@WebFluxTest(controllers=PagingController.class, properties="spring.test.jsontesters.enabled=true")
@ImportAutoConfiguration(JsonTestersAutoConfiguration.class)
public class ReactivePageableParamResolverTest {
	@Autowired
	private WebTestClient testClient;
	@Autowired
	private JacksonTester<JsonNode> jacksonTester;

	public ReactivePageableParamResolverTest() {}

	/**
	 * Tests the fallback setting for paging.
	 */
	@Test
	void fallback()
	{
		testClient.get()
			.uri("/paging-fallback")
			.exchange()
			.expectStatus().is2xxSuccessful()
			.expectBody(JsonNode.class)
			.value(jsonContent -> jsonAssert(jsonContent)
				.hasJsonPathValue("[?(@.page == %d)]", DEFAULT_PARAM_VALUE_PAGE)
				.hasJsonPathValue("[?(@.size == %d)]", DEFAULT_PARAM_VALUE_PAGE_SIZE)
			);
	}

	/**
	 * Tests the setting by {@link PageableDefault}.
	 */
	@Test
	void withPageableDefault()
	{
		testClient.get()
			.uri("/paging")
			.exchange()
			.expectStatus().is2xxSuccessful()
			.expectBody(JsonNode.class)
			.value(jsonContent -> jsonAssert(jsonContent)
				.hasJsonPathValue("[?(@.page == %d)]", 3)
				.hasJsonPathValue("[?(@.size == %d)]", 17)
			);
	}

	/**
	 * Tests the setting by HTTP headers.
	 */
	@Test
	void byHttpHeaders()
	{
		testClient.get()
			.uri("/paging")
			.header(DEFAULT_PARAM_NAME_PAGE, "7")
			.header(DEFAULT_PARAM_NAME_PAGE_SIZE, "7")
			.exchange()
			.expectStatus().is2xxSuccessful()
			.expectBody(JsonNode.class)
			.value(jsonContent -> jsonAssert(jsonContent)
				.hasJsonPathValue("[?(@.page == %d)]", 7)
				.hasJsonPathValue("[?(@.size == %d)]", 7)
			);
	}

	/**
	 * Tests the setting by HTTP query string.
	 */
	@Test
	void byHttpQueryString()
	{
		testClient.get()
			.uri("/paging?page=13&page-size=9")
			.exchange()
			.expectStatus().is2xxSuccessful()
			.expectBody(JsonNode.class)
			.value(jsonContent -> jsonAssert(jsonContent)
				.hasJsonPathValue("[?(@.page == %d)]", 13)
				.hasJsonPathValue("[?(@.size == %d)]", 9)
			);
	}

	private JsonContentAssert jsonAssert(JsonNode json)
	{
		try {
			return assertThat(jacksonTester.write(json));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

@RestController
class PagingController {
	@GetMapping("/paging-fallback")
	Mono<Map<String, Object>> pagingFallback(
		Pageable pageable
	) {
		return Mono.just(Map.<String, Object>of(
			"page", pageable.getPageNumber(),
			"size", pageable.getPageSize()
		));
	}

	@GetMapping("/paging")
	Mono<Map<String, Object>> paging(
		@PageableDefault(page=3, size=17)
		Pageable pageable
	) {
		return Mono.just(Map.<String, Object>of(
			"page", pageable.getPageNumber(),
			"size", pageable.getPageSize()
		));
	}
}
