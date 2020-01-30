package guru.mikelue.misc.lang.data;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.assertj.core.data.Index.atIndex;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import guru.mikelue.misc.lang.tuple.Tuple;

public class CartesianProductTest {
	private final static Logger logger = LoggerFactory.getLogger(CartesianProductTest.class);

	public CartesianProductTest() {}

	/**
	 * Tests every type of tuples(for correct arguments) for folding from Iterables.
	 */
	@ParameterizedTest
	@MethodSource
	void foldByIterable(
		Method foldMethod, Object[] args,
		Tuple[] expectedResult
	) throws Exception {
		logger.info("Tests fold method[{}]({}...) for {} arguments",
			foldMethod.getName(), foldMethod.getParameterTypes()[0].getSimpleName(), args.length);

		@SuppressWarnings("unchecked")
		var testedTuples = (Iterable<Tuple>)foldMethod.invoke(null, args);

		assertThat(testedTuples)
			.containsExactly(expectedResult);
	}
	static Stream<Arguments> foldByIterable()
	{
		return IntStream.range(2, 11)
			.<Tuple.Triplet<
				Method, Object[], Tuple[]
			>>mapToObj(
				n -> Tuple.of(
					getFoldMethod(n, FoldingArgType.Iterable),
					buildIterableArgs(n),
					buildExpectedTuples(n, getTupleSupplier(n))
				)
			)
			.map(t-> arguments(
				t.getValue0(), t.getValue1(), t.getValue2()
			));
	}

	/**
	 * Tests every type of tuples(for correct arguments) for folding from Streams.
	 */
	@ParameterizedTest
	@MethodSource
	void foldByStream(
		Method foldMethod, Object[] args,
		Tuple[] expectedResult
	) throws Exception {
		logger.info("Tests fold method[{}]({}...) for {} arguments",
			foldMethod.getName(), foldMethod.getParameterTypes()[0].getSimpleName(), args.length);

		@SuppressWarnings("unchecked")
		var testedTuples = (Stream<Tuple>)foldMethod.invoke(null, args);

		assertThat(testedTuples)
			.containsExactly(expectedResult);
	}
	static Stream<Arguments> foldByStream()
	{
		return IntStream.range(2, 11)
			.<Tuple.Triplet<
				Method, Object[], Tuple[]
			>>mapToObj(
				n -> Tuple.of(
					getFoldMethod(n, FoldingArgType.Stream),
					buildStreamArgs(n),
					buildExpectedTuples(n, getTupleSupplier(n))
				)
			)
			.map(t-> arguments(
				t.getValue0(), t.getValue1(), t.getValue2()
			));
	}

	private enum FoldingArgType {
		Iterable, Stream;
	}

	private static Supplier<Tuple> getTupleSupplier(int numberOfArguments)
	{
		var args = new Object[numberOfArguments];

		switch (numberOfArguments) {
			case 2:
				return () -> Tuple.ofPair(args);
			case 3:
				return () -> Tuple.ofTriplet(args);
			case 4:
				return () -> Tuple.ofQuartet(args);
			case 5:
				return () -> Tuple.ofQuintet(args);
			case 6:
				return () -> Tuple.ofSextet(args);
			case 7:
				return () -> Tuple.ofSeptet(args);
			case 8:
				return () -> Tuple.ofOctet(args);
			case 9:
				return () -> Tuple.ofEnnead(args);
			case 10:
				return () -> Tuple.ofDecade(args);
		}

		return null;
	}
	private static Tuple[] buildExpectedTuples(int numberOfArguments, Supplier<Tuple> supplier)
	{
		var newTuple = supplier.get();

		for (int i = 0; i < numberOfArguments; i++) {
			newTuple.setValue(i, i + 1);
		}

		return new Tuple[] { newTuple };
	}
	private static Object[] buildStreamArgs(int numberOfArguments)
	{
		var listArgs = buildIterableArgs(numberOfArguments);
		var result = new Object[numberOfArguments];

		for (int i = 0; i < numberOfArguments; i++) {
			result[i] = ((List<?>)listArgs[i]).stream();
		}

		return result;
	}
	private static Object[] buildIterableArgs(int numberOfArguments)
	{
		var result = new Object[numberOfArguments];

		for (int i = 0; i < numberOfArguments; i++) {
			result[i] = List.of(i + 1);
		}

		return result;
	}
	private static Method getFoldMethod(int numberOfArguments, FoldingArgType argType)
	{
		var argClasses = new Class<?>[numberOfArguments];

		switch (argType) {
			case Iterable:
				for (int i = 0; i < numberOfArguments; i++) {
					argClasses[i] = Iterable.class;
				}
				break;
			case Stream:
				for (int i = 0; i < numberOfArguments; i++) {
					argClasses[i] = Stream.class;
				}
				break;
		}

		return MethodUtils.getMatchingMethod(CartesianProduct.class, "fold", argClasses);
	}
}

class CartesianProductImplTest {
	/**
	 * Tests folding by data of streams.
	 */
	@ParameterizedTest
	@MethodSource
	void foldByStream(
		Supplier<Tuple> rowBuilder, Stream<Integer>[] dataSets,
		int expectedSize, Consumer<ListAssert<Tuple>> assertion
	) {
		var testedResult = CartesianProduct.Impl.fold(
			rowBuilder, dataSets
		);

		/**
		 * Assertions
		 */
		var assertedTarget = assertThat(testedResult);
		assertedTarget.hasSize(expectedSize);
		assertion.accept(assertedTarget);
		// :~)
	}
	static Stream<Arguments> foldByStream()
	{
		return Stream.<Tuple.Quartet<
			Supplier<Tuple>, Stream<?>[],
			Integer, Consumer<ListAssert<Tuple>>
		>>of(
			Tuple.of( // Single row
				() -> Tuple.ofPair(new Object[2]),
				new Stream<?>[] {
					Stream.of(17),
					Stream.of(18),
				},
				1,
				assertObj -> assertObj
					.contains(Tuple.of(17, 18), atIndex(0))
			),
			Tuple.of( // Multiple rows
				() -> Tuple.ofTriplet(new Object[3]),
				new Stream<?>[] {
					Stream.of(1, 3, 5),
					Stream.of(2, 4, 6),
					Stream.of(17, 19)
				},
				18,
				assertObj -> assertObj
					.contains(Tuple.of(1, 2, 17), atIndex(0))
					.contains(Tuple.of(5, 6, 19), atIndex(17))
			),
			Tuple.of( // Empty set(no row)
				() -> Tuple.ofTriplet(new Object[3]),
				new Stream<?>[] {
					Stream.of(1, 3, 5),
					Stream.of(),
					Stream.of(17, 19),
				},
				0,
				assertObj -> {}
			),
			Tuple.of( // Empty set of last one(no row)
				() -> Tuple.ofTriplet(new Object[3]),
				new Stream<?>[] {
					Stream.of(1, 3, 5),
					Stream.of(17, 19),
					Stream.of(),
				},
				0,
				assertObj -> {}
			)
		)
			.map(t -> arguments(
				t.getValue0(), t.getValue1(), t.getValue2(), t.getValue3()
			));
	}

	/**
	 * Tests folding by data of iterables.
	 */
	@ParameterizedTest
	@MethodSource
	void foldByIterable(
		Supplier<Tuple> rowBuilder, List<Integer>[] dataSets,
		int expectedSize, Consumer<ListAssert<Tuple>> assertion
	) {
		var rawResult = CartesianProduct.Impl.fold(
			rowBuilder, dataSets
		);

		var testedResult = StreamSupport.stream(
			rawResult.spliterator(), false
		);

		/**
		 * Assertions
		 */
		var assertedTarget = assertThat(testedResult);
		assertedTarget.hasSize(expectedSize);
		assertion.accept(assertedTarget);
		// :~)
	}

	static Stream<Arguments> foldByIterable()
	{
		return Stream.<Tuple.Quartet<
			Supplier<Tuple>, List<?>[],
			Integer, Consumer<ListAssert<Tuple>>
		>>of(
			Tuple.of( // Single row
				() -> Tuple.ofPair(new Object[2]),
				new List<?>[] {
					List.of(17),
					List.of(18),
				},
				1,
				assertObj -> assertObj
					.contains(Tuple.of(17, 18), atIndex(0))
			),
			Tuple.of( // Multiple rows
				() -> Tuple.ofTriplet(new Object[3]),
				new List<?>[] {
					List.of(1, 3, 5),
					List.of(2, 4, 6),
					List.of(17, 19)
				},
				18,
				assertObj -> assertObj
					.contains(Tuple.of(1, 2, 17), atIndex(0))
					.contains(Tuple.of(5, 6, 19), atIndex(17))
			),
			Tuple.of( // Empty set(no row)
				() -> Tuple.ofTriplet(new Object[3]),
				new List<?>[] {
					List.of(1, 3, 5),
					List.of(),
					List.of(17, 19),
				},
				0,
				assertObj -> {}
			),
			Tuple.of( // Empty set of last one(no row)
				() -> Tuple.ofTriplet(new Object[3]),
				new List<?>[] {
					List.of(1, 3, 5),
					List.of(17, 19),
					List.of(),
				},
				0,
				assertObj -> {}
			)
		)
			.map(t -> arguments(
				t.getValue0(), t.getValue1(), t.getValue2(), t.getValue3()
			 ));
	}
}
