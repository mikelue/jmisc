package guru.mikelue.misc.lang.tuple;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import guru.mikelue.misc.lang.tuple.Tuple.*;

public class TupleTest {
	private final Logger logger = LoggerFactory.getLogger(TupleTest.class);

	public TupleTest() {}

	/**
	 * Tests the setting of value(correct index) by static method of {@link Tuple#of}.
	 */
	@ParameterizedTest
	@MethodSource("allTypesOfTupleByArgumentNumber")
	void of(int numberOfArgs)
	{
		var testedTuple = TupleGenerator.of(numberOfArgs);

		logger.info("Testing \"getValueN()\" for {}", testedTuple.getClass().getSimpleName());

		for (int i = 0; i < numberOfArgs; i++) {
			assertThat((Object)testedTuple)
				.extracting(String.format("value%d", i))
				.isEqualTo(TupleGenerator.arguments[i]);
		}
	}

	/**
	 * Tests the setting of elements(correct index) by numbered method.
	 */
	@ParameterizedTest
	@MethodSource("allTypesOfTupleByArgumentNumber")
	void setValueN(int numberOfArgs)
	{
		var testedTuple = TupleGenerator.of(numberOfArgs);

		logger.info("Testing \"setAtN()\" for {}", testedTuple.getClass().getSimpleName());

		for (int i = 0; i < numberOfArgs; i++) {
			TupleGenerator.setValueByNumberedMethod(testedTuple, String.format("setAt%d", i), 101);

			assertThat((Object)testedTuple)
				.extracting(String.format("value%d", i))
				.isEqualTo(101);
		}
	}

	/**
	 * Tests the cloning of a tuple(typed).
	 */
	@ParameterizedTest
	@MethodSource("allTypesOfTupleByArgumentNumber")
	void typedClone(int numberOfArgs) throws Exception
	{
		var sampleTuple = TupleGenerator.of(numberOfArgs);

		logger.info("Testing \"typedClone()\" for {}", sampleTuple.getClass().getSimpleName());

		var cloned = (Tuple)MethodUtils.invokeMethod(sampleTuple, "typedClone");

		cloned.setValue(0, "ClonedValue");

		assertThat(sampleTuple.<Object>getValue(0))
			.isNotEqualTo(cloned.getValue(0));
	}
	static Stream<Arguments> typedClone()
	{
		return TupleGenerator.allTypesOfTuple.stream()
			.map(
				tupleType -> {
					try {
						return MethodUtils.getMatchingMethod(
							tupleType, "typedClone"
						);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			)
			.map(Arguments::arguments);
	}

	static Stream<Arguments> allTypesOfTupleByArgumentNumber()
	{
		return IntStream.range(
			1, 11
		)
			.mapToObj(c -> arguments(c));
	}
}

class TupleGenerator {
	static final Object[] arguments = {
		"S1", (byte)1, (short)2, 3, (long)4,
		'A', "S2", (byte)5.5f, (short)6.6d, 7
	};

	static final List<Class<? extends Tuple>> allTypesOfTuple =
		List.of(
			 Unit.class, Pair.class, Triplet.class, Quartet.class, Quintet.class,
			 Sextet.class, Septet.class, Octet.class, Ennead.class, Decade.class
		);

	static Tuple of(int numberOfArgs)
	{
		try {
			return (Tuple)MethodUtils.invokeStaticMethod(
				Tuple.class, "of", Arrays.copyOfRange(arguments, 0, numberOfArgs)
			);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static void setValueByNumberedMethod(Tuple instance, String methodName, Object value)
	{
		try {
			MethodUtils.invokeMethod(instance, methodName, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
