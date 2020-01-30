package guru.mikelue.misc.lang.tuple;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TupleImplTest {
	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(TupleImplTest.class);

	public TupleImplTest() {}

	/**
	 * Tests containsAll method(which tests contains() also).
	 */
	@ParameterizedTest
	@MethodSource
	void containsAll(boolean expected, Object a, Object b, Object nullableValue)
	{
		var testedTuple = Tuple.of(10, 20, 30, nullableValue, 40, 50);

		assertThat(testedTuple.containsAll(a, b))
			.isEqualTo(expected);
	}
	static Arguments[] containsAll()
	{
		return new Arguments[] {
			arguments(true, 20, 30, 110),
			arguments(true, 10, 50, null),
			arguments(false, 10, 11, 110),
			arguments(false, 10, null, 110),
			arguments(true, 10, null, null),
		};
	}

	/**
	 * Tests the finding for index of matched object.
	 */
	@ParameterizedTest
	@MethodSource
	void indexOf(Tuple sampleTuple, Integer searchValue, int expectedResult)
	{
		assertThat(sampleTuple.indexOf(searchValue))
			.isEqualTo(expectedResult);
	}
	static Arguments[] indexOf()
	{
		var tupleHasNull = Tuple.of(3, 8, null, null, 5, 8);
		var tupleHasNoNull = Tuple.of(3, 8, 5, 8);

		return new Arguments[] {
			arguments(tupleHasNull, 8, 1),
			arguments(tupleHasNull, 5, 4),
			arguments(tupleHasNull, -10, -1),
			arguments(tupleHasNull, null, 2),
			arguments(tupleHasNoNull, null, -1),
		};
	}

	/**
	 * Tests the finding for index of matched object(from last elements).
	 */
	@ParameterizedTest
	@MethodSource
	void lastIndexOf(Tuple sampleTuple, Integer searchValue, int expectedResult)
	{
		assertThat(sampleTuple.lastIndexOf(searchValue))
			.isEqualTo(expectedResult);
	}
	static Arguments[] lastIndexOf()
	{
		var tupleHasNull = Tuple.of(3, 7, 5, null, null, 7);
		var tupleHasNoNull = Tuple.of(3, 7, 5, 7);

		return new Arguments[] {
			arguments(tupleHasNull, 7, 5),
			arguments(tupleHasNull, 5, 2),
			arguments(tupleHasNull, -10, -1),
			arguments(tupleHasNull, null, 4),
			arguments(tupleHasNoNull, null, -1),
		};
	}

	/**
	 * Tests the conversion from {@link Tuple} to "Object[]".
	 */
	@Test
	void toArray()
	{
		var sampleTuple = Tuple.of(10, null, 45, 89);

		/**
		 * Asserts the array elements is contained in tuple
		 */
		var testedArray = sampleTuple.toArray();
		assertThat(sampleTuple.toArray())
			.containsExactly(10, null, 45, 89);
		// :~)

		/**
		 * Asserts the array is cloned from the tuple
		 */
		testedArray[1] = 15;
		assertThat(sampleTuple.toArray())
			.containsExactly(10, null, 45, 89);
		// :~)
	}

	/**
	 * Tests the conversion from {@link Tuple} to {@link List.}
	 */
	@Test
	void toList()
	{
		var sampleTuple = Tuple.of(11, 46, null, 93);

		/**
		 * Asserts the list elements is contained in tuple
		 */
		assertThat(sampleTuple.toList())
			.containsExactly(11, 46, null, 93);
		// :~)
	}

	/**
	 * Checks the excpetion for out of bound.
	 */
	@Test
	void checkOutOfBound()
	{
		var testedTuple = Tuple.of(10, 20);

		assertThatThrownBy(
			() -> testedTuple.getValue(-1)
		)
			.isInstanceOf(IndexOutOfBoundsException.class);

		assertThatThrownBy(
			() -> testedTuple.getValue(2)
		)
			.isInstanceOf(IndexOutOfBoundsException.class);
	}
}
