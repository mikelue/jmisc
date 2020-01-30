package guru.mikelue.misc.lang.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.assertj.core.api.IterableAssert;
import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ObjectArrayAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import guru.mikelue.misc.lang.tuple.Tuple;

public class BagDataBuilderBaseTest {
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(BagDataBuilderBaseTest.class);

	public BagDataBuilderBaseTest() {}

	/**
	 * Tests default method for building of arrays(null if size is zero).
	 */
	@ParameterizedTest
	@MethodSource
	void buildArrayOrNull(SampleBuilder testedBuilder, int size, Consumer<ObjectArrayAssert<?>> assertion)
	{
		var assertedTarget = assertThat(testedBuilder.buildArrayOrNull(size));
		assertion.accept(assertedTarget);
	}
	static Stream<Arguments> buildArrayOrNull()
	{
		var sampleBuilder = new SampleBuilder();

		return Stream.<Tuple.Pair<Integer, Consumer<ObjectArrayAssert<?>>>>of(
			Tuple.of(
				0,
				assertion -> assertion.isNull()
			),
			Tuple.of(
				SampleBuilder.FIX_SIZE,
				assertion -> assertion.hasSize(SampleBuilder.FIX_SIZE)
			)
		)
			.map(
				t -> arguments(sampleBuilder, t.getValue0(), t.getValue1())
			);
	}

	/**
	 * Tests default method for building of lists(null if size is zero).
	 */
	@ParameterizedTest
	@MethodSource
	void buildListOrNull(SampleBuilder testedBuilder, int size, Consumer<ListAssert<?>> assertion)
	{
		var assertedTarget = assertThat(testedBuilder.buildListOrNull(size));
		assertion.accept(assertedTarget);
	}
	static Stream<Arguments> buildListOrNull()
	{
		var sampleBuilder = new SampleBuilder();

		return Stream.<Tuple.Pair<Integer, Consumer<ListAssert<?>>>>of(
			Tuple.of(
				0,
				assertion -> assertion.isNull()
			),
			Tuple.of(
				SampleBuilder.FIX_SIZE,
				assertion -> assertion.hasSize(SampleBuilder.FIX_SIZE)
			)
		)
			.map(
				t -> arguments(sampleBuilder, t.getValue0(), t.getValue1())
			);
	}

	/**
	 * Tests default method for building of sets(null if size is zero).
	 */
	@ParameterizedTest
	@MethodSource
	void buildSetOrNull(SampleBuilder testedBuilder, int size, Consumer<IterableAssert<?>> assertion)
	{
		var assertedTarget = assertThat(testedBuilder.buildSetOrNull(size));
		assertion.accept(assertedTarget);
	}
	static Stream<Arguments> buildSetOrNull()
	{
		var sampleBuilder = new SampleBuilder();

		return Stream.<Tuple.Pair<Integer, Consumer<IterableAssert<?>>>>of(
			Tuple.of(
				0,
				assertion -> assertion.isNull()
			),
			Tuple.of(
				SampleBuilder.FIX_SIZE,
				assertion -> assertion.hasSize(SampleBuilder.FIX_SIZE)
			)
		)
			.map(
				t -> arguments(sampleBuilder, t.getValue0(), t.getValue1())
			);
	}
}

class SampleBuilder implements BagDataBuilderBase<Integer> {
	final static int FIX_SIZE = 3;

	@Override
	public Integer[] buildArray(int size)
	{
		return new Integer[] { 1, 2, 3 };
	}

	@Override
	public List<Integer> buildList(int size)
	{
		return List.of(buildArray(size));
	}

	@Override
	public Set<Integer> buildSet(int size)
	{
		return Set.of(buildArray(size));
	}
}
