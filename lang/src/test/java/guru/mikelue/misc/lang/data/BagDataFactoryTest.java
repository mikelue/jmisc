package guru.mikelue.misc.lang.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import guru.mikelue.misc.lang.tuple.Tuple;

public class BagDataFactoryTest {
	public BagDataFactoryTest() {}

	enum EmptyType {
		Empty, Null;
	}

	/**
	 * Tests the building of arrays for all of supported types.
	 */
	@ParameterizedTest
	@MethodSource
	void buildArrays(Function<Integer, Object> builderImpl, Class<?> expectedClass)
	{
		var result = builderImpl.apply(3);

		assertThat(ArrayUtils.getLength(result))
			.isEqualTo(3);
		assertThat(result.getClass())
			.isEqualTo(expectedClass);
	}
	static Stream<Arguments> buildArrays()
	{
		return Stream.<Tuple.Pair<Function<Integer, Object>, Class<?>>>of(
			Tuple.of(
				c -> BagDataFactory.Arrays.buildObjects(c, Triceratops::new),
				Object[].class
			),
			Tuple.of(
				c -> BagDataFactory.Arrays.buildStrings(c, i -> "Cool"),
				String[].class
			),
			Tuple.of(
				c -> BagDataFactory.Arrays.buildBooleans(c, i -> Boolean.TRUE),
				boolean[].class
			),
			Tuple.of(
				c -> BagDataFactory.Arrays.buildBooleansObj(c, i -> Boolean.TRUE),
				Boolean[].class
			),
			Tuple.of(
				c -> BagDataFactory.Arrays.buildChars(c, i -> 'A'),
				char[].class
			),
			Tuple.of(
				c -> BagDataFactory.Arrays.buildCharsObj(c, i -> 'B'),
				Character[].class
			),
			Tuple.of(
				c -> BagDataFactory.Arrays.buildBytes(c, i -> Byte.MIN_VALUE),
				byte[].class
			),
			Tuple.of(
				c -> BagDataFactory.Arrays.buildBytesObj(c, i -> Byte.MIN_VALUE),
				Byte[].class
			),
			Tuple.of(
				c -> BagDataFactory.Arrays.buildShorts(c, i -> Short.MIN_VALUE),
				short[].class
			),
			Tuple.of(
				c -> BagDataFactory.Arrays.buildShortsObj(c, i -> Short.MIN_VALUE),
				Short[].class
			),
			Tuple.of(
				c -> BagDataFactory.Arrays.buildInts(c, i -> Integer.MIN_VALUE),
				int[].class
			),
			Tuple.of(
				c -> BagDataFactory.Arrays.buildIntsObj(c, i -> Integer.MIN_VALUE),
				Integer[].class
			),
			Tuple.of(
				c -> BagDataFactory.Arrays.buildLongs(c, i -> Long.MIN_VALUE),
				long[].class
			),
			Tuple.of(
				c -> BagDataFactory.Arrays.buildLongsObj(c, i -> Long.MIN_VALUE),
				Long[].class
			),
			Tuple.of(
				c -> BagDataFactory.Arrays.buildFloats(c, i -> Float.MIN_VALUE),
				float[].class
			),
			Tuple.of(
				c -> BagDataFactory.Arrays.buildFloatsObj(c, i -> Float.MIN_VALUE),
				Float[].class
			),
			Tuple.of(
				c -> BagDataFactory.Arrays.buildDoubles(c, i -> Double.MIN_VALUE),
				double[].class
			),
			Tuple.of(
				c -> BagDataFactory.Arrays.buildDoublesObj(c, i -> Double.MIN_VALUE),
				Double[].class
			)
		)
			.map(t -> arguments(t.getValue0(), t.getValue1()));
	}

	/**
	 * Tests the building of arrays(with null version).
	 */
	@ParameterizedTest
	@MethodSource
	void buildArrayOrNull(EmptyType emptyType, int count, Object expectedValue)
	{
		Object testedResult = null;

		switch (emptyType) {
			case Empty:
				testedResult = BagDataFactory.buildArray(count, Triceratops::new, new Triceratops[0]);
				break;
			case Null:
				testedResult = BagDataFactory.buildArrayOrNull(count, Triceratops::new, new Triceratops[0]);
				break;
		}

		assertThat(testedResult).isEqualTo(expectedValue);
	}
	static Arguments[] buildArrayOrNull()
	{
		return new Arguments[] {
			arguments(EmptyType.Empty, 2, new Triceratops[] { new Triceratops(0), new Triceratops(1) } ),
			arguments(EmptyType.Empty, 0, new Triceratops[0]),
			arguments(EmptyType.Null, 2, new Triceratops[] { new Triceratops(0), new Triceratops(1) }),
			arguments(EmptyType.Null, 0, null),
		};
	}

	/**
	 * Tests the building of lists(with null version).
	 */
	@ParameterizedTest
	@MethodSource
	void buildListOrNull(EmptyType emptyType, int count, Object expectedValue)
	{
		Object testedResult = null;

		switch (emptyType) {
			case Empty:
				testedResult = BagDataFactory.buildList(count, Triceratops::new);
				break;
			case Null:
				testedResult = BagDataFactory.buildListOrNull(count, Triceratops::new);
				break;
		}

		assertThat(testedResult).isEqualTo(expectedValue);
	}
	static Arguments[] buildListOrNull()
	{
		return new Arguments[] {
			arguments(EmptyType.Empty, 2, List.of(new Triceratops(0), new Triceratops(1))),
			arguments(EmptyType.Empty, 0, List.of()),
			arguments(EmptyType.Null, 2, List.of(new Triceratops(0), new Triceratops(1))),
			arguments(EmptyType.Null, 0, null),
		};
	}

	/**
	 * Tests the building of sets(with null version)..
	 */
	@ParameterizedTest
	@MethodSource
	void buildSetOrNull(EmptyType emptyType, int count, Object expectedValue)
	{
		Object testedResult = null;

		switch (emptyType) {
			case Empty:
				testedResult = BagDataFactory.buildSet(count, Triceratops::new);
				break;
			case Null:
				testedResult = BagDataFactory.buildSetOrNull(count, Triceratops::new);
				break;
		}

		assertThat(testedResult).isEqualTo(expectedValue);
	}
	static Arguments[] buildSetOrNull()
	{
		return new Arguments[] {
			arguments(EmptyType.Empty, 2, Set.of(new Triceratops(0), new Triceratops(1))),
			arguments(EmptyType.Empty, 0, Set.of()),
			arguments(EmptyType.Null, 2, Set.of(new Triceratops(0), new Triceratops(1))),
			arguments(EmptyType.Null, 0, null),
		};
	}

	/**
	 * Tests the building of maps(with null version)..
	 */
	@ParameterizedTest
	@MethodSource
	void buildMapOrNull(EmptyType emptyType, int count, Object expectedValue)
	{
		Object testedResult = null;

		switch (emptyType) {
			case Empty:
				testedResult = BagDataFactory.buildMap(count, Integer::valueOf, Triceratops::new);
				break;
			case Null:
				testedResult = BagDataFactory.buildMapOrNull(count, Integer::valueOf, Triceratops::new);
				break;
		}

		assertThat(testedResult).isEqualTo(expectedValue);
	}
	static Arguments[] buildMapOrNull()
	{
		return new Arguments[] {
			arguments(EmptyType.Empty, 2, Map.of(0, new Triceratops(0), 1, new Triceratops(1))),
			arguments(EmptyType.Empty, 0, Map.of()),
			arguments(EmptyType.Null, 2, Map.of(0, new Triceratops(0), 1, new Triceratops(1))),
			arguments(EmptyType.Null, 0, null),
		};
	}

	/**
	 * Tests the building of stream of data
	 */
	@ParameterizedTest
	@MethodSource
	void buildStream(int size, int expectedSum)
	{
		var testedValue = BagDataFactory.buildStream(size, i -> Integer.valueOf(i + 2))
			.reduce(
				(a, b )-> a + b
			)
			.orElse(0);

		assertThat(testedValue).isEqualTo(expectedSum);
	}
	static Arguments[] buildStream()
	{
		return new Arguments[] {
			arguments(2, 5),
			arguments(0, 0),
		};
	}
}

class Triceratops {
	final int value;

	Triceratops(int value)
	{
		this.value = value;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) { return false; }
		if (obj == this) { return true; }
		if (obj.getClass() != getClass()) {
			return false;
		}

		var rhs = (Triceratops)obj;
		return new EqualsBuilder()
			.append(this.value, rhs.value)
			.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(104281, 6449)
			.append(value)
		.toHashCode();
	}

	@Override
	public String toString()
	{
		return String.format("Triceratops[%d]", value);
	}
}
