package guru.mikelue.misc.springframework.data.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.JsonTestersAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContentAssert;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import reactor.core.publisher.Mono;

@WebFluxTest(controllers=SortController.class, properties="spring.test.jsontesters.enabled=true")
@ImportAutoConfiguration(JsonTestersAutoConfiguration.class)
public class ReactiveSortParamResolverTest {
	@Autowired
	private WebTestClient testClient;
	@Autowired
	private JacksonTester<JsonNode> jacksonTester;

	public ReactiveSortParamResolverTest() {}

	/**
	 * Tests the paging parameters by default fallback.
	 */
	@Test
	void defaultFallbackPaging()
	{
		testClient.get()
			.uri("/paging-fallback")
			.exchange()
			.expectStatus().is2xxSuccessful()
			.expectBody(JsonNode.class)
			.value(jsonContent -> jsonAssert(jsonContent)
				.hasJsonPathValue("$[?(@.sorted == false)]")
			);
	}

	/**
	 * Tests the paging parameters by {@link SortDefault}.
	 */
	@Test
	void defaultSort()
	{
		testClient.get()
			.uri("/paging")
			.exchange()
			.expectStatus().is2xxSuccessful()
			.expectBody(JsonNode.class)
			.value(jsonContent -> jsonAssert(jsonContent)
				.hasJsonPathValue("$.sort[0][?(@.property == 'name')]")
				.hasJsonPathValue("$.sort[0][?(@.descending == true)]")
				.hasJsonPathValue("$.sort[1][?(@.property == 'age')]")
				.hasJsonPathValue("$.sort[1][?(@.ascending == true)]")
			);
	}

	/**
	 * Tests the paging parameters by HTTP headers.
	 */
	@Test
	void byHttpHeader()
	{
		testClient.get()
			.uri("/paging")
			.header("page-sort", "v1,v2,v3")
			.exchange()
			.expectStatus().is2xxSuccessful()
			.expectBody(JsonNode.class)
			.value(jsonContent -> jsonAssert(jsonContent)
				.hasJsonPathValue("$.sort[0][?(@.property == 'v1')]")
				.hasJsonPathValue("$.sort[1][?(@.property == 'v2')]")
				.hasJsonPathValue("$.sort[2][?(@.property == 'v3')]")
			);
	}

	/**
	 * Tests the paging parameters by HTTP query string.
	 */
	@Test
	void byHttpQueryString()
	{
		testClient.get()
			.uri("/paging?page-sort=g1,g2,g3")
			.exchange()
			.expectStatus().is2xxSuccessful()
			.expectBody(JsonNode.class)
			.value(jsonContent -> jsonAssert(jsonContent)
				.hasJsonPathValue("$.sort[0][?(@.property == 'g1')]")
				.hasJsonPathValue("$.sort[1][?(@.property == 'g2')]")
				.hasJsonPathValue("$.sort[2][?(@.property == 'g3')]")
			);
	}

	/**
	 * Tests the different default direction.
	 */
	@Test
	void changeDefaultDirection()
	{
		testClient.get()
			.uri("/paging-default-direction?page-sort=z1,z2")
			.exchange()
			.expectStatus().is2xxSuccessful()
			.expectBody(JsonNode.class)
			.value(jsonContent -> jsonAssert(jsonContent)
				.hasJsonPathValue("$.sort[0][?(@.property == 'z1')]")
				.hasJsonPathValue("$.sort[0][?(@.descending == true)]")
				.hasJsonPathValue("$.sort[1][?(@.property == 'z2')]")
				.hasJsonPathValue("$.sort[1][?(@.descending == true)]")
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
class SortController {
	@GetMapping("/paging-fallback")
	Mono<Map<String, Object>> pagingFallback(
		Sort sort
	) {
		return Mono.just(Map.<String, Object>of(
			"sorted", sort.isSorted()
		));
	}

	@GetMapping("/paging")
	Mono<Map<String, Object>> paging(
		@SortDefault(sort={"name:desc", "age"}) Sort sort
	) {
		return Mono.just(Map.<String, Object>of(
			"sorted", sort.isSorted(),
			"sort", sort.toList()
		));
	}

	@GetMapping("/paging-default-direction")
	Mono<Map<String, Object>> pagingDefaultDirection(
		@SortDefault(direction=Sort.Direction.DESC) Sort sort
	) {
		return Mono.just(Map.<String, Object>of(
			"sorted", sort.isSorted(),
			"sort", sort.toList()
		));
	}
}
