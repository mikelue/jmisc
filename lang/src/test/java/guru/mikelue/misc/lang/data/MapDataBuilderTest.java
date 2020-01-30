package guru.mikelue.misc.lang.data;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.assertj.core.api.MapAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import guru.mikelue.misc.lang.tuple.Tuple;

public class MapDataBuilderTest {
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(MapDataBuilderTest.class);

	public MapDataBuilderTest() {}

	/**
	 * Tests builder object for maps.
	 */
	@ParameterizedTest
	@MethodSource
	void buildOrNull(
		Supplier<Map<Integer, Integer>> valueGetter,
		Consumer<MapAssert<Integer, Integer>> assertion
	) {
		var assertedTarget = assertThat(valueGetter.get());
		assertion.accept(assertedTarget);
	}
	static Stream<Arguments> buildOrNull()
	{
		var testedBuilder = MapDataBuilder.of(
			i -> i + 1, i -> i * 2 + 1
		);

		return Stream.<Tuple.Pair<
			Supplier<Map<Integer, Integer>>,
			Consumer<MapAssert<Integer, Integer>>
		>>of(
			Tuple.of(
				() -> testedBuilder.build(3),
				assertion -> assertion.containsKeys(1, 2, 3)
			),
			Tuple.of(
				() -> testedBuilder.build(0),
				assertion -> assertion.isEmpty()
			),
			Tuple.of(
				() -> testedBuilder.buildOrNull(3),
				assertion -> assertion.containsValues(1, 3, 5)
			),
			Tuple.of(
				() -> testedBuilder.buildOrNull(0),
				assertion -> assertion.isNull()
			)
		)
			.map(t -> arguments(t.getValue0(), t.getValue1()));
	}
}
