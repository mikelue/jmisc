package guru.mikelue.misc.lang.tuple;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.apache.commons.lang3.math.NumberUtils;
import org.jeasy.random.randomizers.FirstNameRandomizer;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import guru.mikelue.misc.lang.data.BagDataFactory;

public class TupleOperatorsTest {
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(TupleOperatorsTest.class);

	public TupleOperatorsTest() {}

	/**
	 * Tests the adding of two tuples.
	 */
	@ParameterizedTest
	@MethodSource
	void addAs(
		BiFunction<Tuple, Tuple, ? extends Tuple> operator,
		int expectedCount
	) {
		var dataSampler = new ArrayDataSampler();

		var testedResult = operator.apply(dataSampler.getFirstPart(), dataSampler.getSecondPart());

		assertThat((Iterable<Object>)testedResult)
			.containsExactly(dataSampler.getExepctedData(expectedCount));
	}
	static Stream<Arguments> addAs()
	{
		return Stream.<Tuple.Pair<
			BiFunction<Tuple, Tuple, ? extends Tuple>,
			Integer
		>>of(
			Tuple.of(TupleOperators::addAsPair, 2),
			Tuple.of(TupleOperators::addAsTriplet, 3),
			Tuple.of(TupleOperators::addAsQuartet, 4),
			Tuple.of(TupleOperators::addAsQuintet, 5),
			Tuple.of(TupleOperators::addAsSextet, 6),
			Tuple.of(TupleOperators::addAsSeptet, 7),
			Tuple.of(TupleOperators::addAsOctet, 8),
			Tuple.of(TupleOperators::addAsEnnead, 9),
			Tuple.of(TupleOperators::addAsDecade, 10)
		)
			.map(
				t -> arguments(t.getValue0(), t.getValue1())
			);
	}

	/**
	 * Tests the adding of two tuples which
	 * the added elements are put in front of certain position of left tuple.
	 */
	@ParameterizedTest
	@MethodSource
	void addAtAs(
		AddAt operator, int atPos,
		Tuple.Quartet<Integer, Integer, Integer, Integer> expectedIndex
	) {
		var dataSampler = new ArrayDataSampler();

		var testedResult = operator.doAddAt(
			dataSampler.getFirstPart(), dataSampler.getSecondPart(),
			atPos
		);

		assertThat((Iterable<Object>)testedResult)
			.containsExactly(dataSampler.getExepctedData(
				expectedIndex.getValue0(),
				expectedIndex.getValue1(),
				expectedIndex.getValue2(),
				expectedIndex.getValue3()
			));
	}
	static Stream<Arguments> addAtAs()
	{
		/**
		 * Append to head of left tuple
		 */
		var allCases = Stream.<Tuple.Pair<
				AddAt, Tuple.Quartet<Integer, Integer, Integer, Integer>
			>>of(
				Tuple.of(TupleOperators::addAtAsPair, Tuple.of(0, 2, 0, 0)),
				Tuple.of(TupleOperators::addAtAsTriplet, Tuple.of(0, 3, 0, 0)),
				Tuple.of(TupleOperators::addAtAsQuartet, Tuple.of(0, 3, 1, 0)),
				Tuple.of(TupleOperators::addAtAsQuintet, Tuple.of(0, 3, 2, 0)),
				Tuple.of(TupleOperators::addAtAsSextet, Tuple.of(0, 3, 3, 0)),
				Tuple.of(TupleOperators::addAtAsSeptet, Tuple.of(0, 3, 3, 1)),
				Tuple.of(TupleOperators::addAtAsOctet, Tuple.of(0, 3, 3, 2)),
				Tuple.of(TupleOperators::addAtAsEnnead, Tuple.of(0, 3, 3, 3)),
				Tuple.of(TupleOperators::addAtAsDecade, Tuple.of(0, 3, 3, 4))
			)
				.map(
					t -> Tuple.of(t.getValue0(), 0, t.getValue1())
				);
		// :~)

		/**
		 * Append to last of left tuple
		 */
		allCases = Stream.concat(
			allCases,
			Stream.<Tuple.Triplet<
				AddAt, Integer,
				Tuple.Quartet<Integer, Integer, Integer, Integer>
			>>of(
				Tuple.of(TupleOperators::addAtAsPair, 2, Tuple.of(2, 0, 0, 0)),
				Tuple.of(TupleOperators::addAtAsTriplet, 3, Tuple.of(3, 0, 0, 0)),
				Tuple.of(TupleOperators::addAtAsQuartet, 4, Tuple.of(3, 1, 0, 0)),
				Tuple.of(TupleOperators::addAtAsQuintet, 5, Tuple.of(3, 2, 0, 0)),
				Tuple.of(TupleOperators::addAtAsSextet, 6, Tuple.of(3, 3, 0, 0)),
				Tuple.of(TupleOperators::addAtAsSeptet, 7, Tuple.of(3, 3, 0, 1)),
				Tuple.of(TupleOperators::addAtAsOctet, 8, Tuple.of(3, 3, 0, 2)),
				Tuple.of(TupleOperators::addAtAsEnnead, 9, Tuple.of(3, 3, 0, 3)),
				Tuple.of(TupleOperators::addAtAsDecade, 10, Tuple.of(3, 3, 0, 4))
			)
		);
		// :~)

		/**
		 * Append to mid of left tuple
		 */
		allCases = Stream.concat(
			allCases,
			Stream.<Tuple.Pair<
				AddAt,
				Tuple.Quartet<Integer, Integer, Integer, Integer>
			>>of(
				Tuple.of(TupleOperators::addAtAsPair, Tuple.of(1, 1, 0, 0)),
				Tuple.of(TupleOperators::addAtAsTriplet, Tuple.of(1, 2, 0, 0)),
				Tuple.of(TupleOperators::addAtAsQuartet, Tuple.of(1, 3, 0, 0)),
				Tuple.of(TupleOperators::addAtAsQuintet, Tuple.of(1, 3, 1, 0)),
				Tuple.of(TupleOperators::addAtAsSextet, Tuple.of(1, 3, 2, 0)),
				Tuple.of(TupleOperators::addAtAsSeptet, Tuple.of(1, 3, 2, 1)),
				Tuple.of(TupleOperators::addAtAsOctet, Tuple.of(1, 3, 2, 2)),
				Tuple.of(TupleOperators::addAtAsEnnead, Tuple.of(1, 3, 2, 3)),
				Tuple.of(TupleOperators::addAtAsDecade, Tuple.of(1, 3, 2, 4))
			)
				.map(
					t -> Tuple.of(t.getValue0(), 1, t.getValue1())
				)
		);
		// :~)

		return allCases.map(
			t -> arguments(t.getValue0(), t.getValue1(), t.getValue2())
		);
	}

	@FunctionalInterface
	interface AddAt {
		Tuple doAddAt(Tuple left, Tuple right, int atPos);
	}
}

class ArrayDataSampler {
	private Integer[] firstPart;
	private String[] secondPart;

	ArrayDataSampler()
	{
		var intRandomizer = new IntegerRangeRandomizer(1, 100);
		firstPart = BagDataFactory.buildArray(3, i -> intRandomizer.getRandomValue(), new Integer[0]);

		var stringRandomizer = new FirstNameRandomizer();
		secondPart = BagDataFactory.buildArray(3, i -> stringRandomizer.getRandomValue(), new String[0]);
	}

	Tuple.Triplet<Integer, Integer, Integer> getFirstPart()
	{
		return Tuple.of(firstPart[0], firstPart[1], firstPart[2]);
	}
	Tuple.Triplet<String, String, String> getSecondPart()
	{
		return Tuple.of(secondPart[0], secondPart[1], secondPart[2]);
	}
	Object[] getExepctedData(int count)
	{
		var expectedData = new Object[count];

		int length = 0;
		int newIndex = 0;

		length = NumberUtils.min(count, firstPart.length);
		System.arraycopy(firstPart, 0, expectedData, 0, length);
		newIndex += length;

		length = NumberUtils.min(count - newIndex, secondPart.length);
		System.arraycopy(secondPart, 0, expectedData, newIndex, length);

		return expectedData;
	}
	/**
	 * Gets the expected data
	 *
	 * @param part1 From 1st part
	 * @param part2 From 2nd part
	 * @param part3 From the rest of 1st part
	 * @param nullPart Filling of null elements
	 */
	Object[] getExepctedData(int part1, int part2, int part3, int nullPart)
	{
		var expectedData = new Object[part1 + part2 + part3 + nullPart];

		int newIndex = 0;

		System.arraycopy(firstPart, 0, expectedData, 0, part1);
		newIndex += part1;

		System.arraycopy(secondPart, 0, expectedData, newIndex, part2);
		newIndex += part2;

		System.arraycopy(firstPart, part1, expectedData, newIndex, part3);
		newIndex += part3;

		System.arraycopy(new Object[nullPart], 0, expectedData, newIndex, nullPart);

		return expectedData;
	}
}
