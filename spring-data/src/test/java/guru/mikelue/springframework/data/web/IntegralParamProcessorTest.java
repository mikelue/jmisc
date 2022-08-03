package guru.mikelue.springframework.data.web;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.apache.commons.lang3.tuple.ImmutablePair;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class IntegralParamProcessorTest {
	private ConversionService conversionService = new DefaultConversionService();

	public IntegralParamProcessorTest() {}

	/**
	 * Tests the getting/conversion of params from HTTP header and HTTP query string.
	 */
	private final static String PARAM_NAME = "sample-size";

	@ParameterizedTest
	@MethodSource
	void getParamOrDefault(
		Consumer<MockServerHttpRequest.BaseBuilder<?>> builderSetup,
		int expectedValue
	) {
		/**
		 * Prepares HTTP request(mocked)
		 */
		var requestBuilder = MockServerHttpRequest.get("/something");
		builderSetup.accept(requestBuilder);
		var mockRequest = requestBuilder.build();
		// :~)

		var testedProcessor = new IntegralParamProcessor(
			conversionService, mockRequest
		);

		/**
		 * Asserts the result
		 */
		assertThat(
			testedProcessor.getParamOrDefault(PARAM_NAME, -1)
		).isEqualTo(expectedValue);
		// :~)
	}
	static Arguments[] getParamOrDefault()
	{
		BiFunction<
			Consumer<MockServerHttpRequest.BaseBuilder<?>>,
			Integer,
			ImmutablePair<
				Consumer<MockServerHttpRequest.BaseBuilder<?>>,
				Integer
			>
		> ofPair = ImmutablePair::of;

		var testCases = Stream.of(
			ofPair.apply( // Nothing
				b -> {}, -1
			),
			ofPair.apply(
				b -> b.header(PARAM_NAME, "90"), 90
			),
			ofPair.apply(
				b -> b.header(PARAM_NAME, ""), -1
			),
			ofPair.apply(
				b -> b.queryParam(PARAM_NAME, "33"), 33
			),
			ofPair.apply(
				b -> b.queryParam(PARAM_NAME, ""), -1
			),
			ofPair.apply( // Use HTTP header with given priority
				b -> b
					.header(PARAM_NAME, "23")
					.queryParam(PARAM_NAME, "71"),

				23
			)
		);

		return testCases
			.map(pair -> arguments(pair.left, pair.right))
			.toArray(Arguments[]::new);
	}
}
