package guru.mikelue.misc.hibernate.postgres;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import guru.mikelue.misc.testlib.assertj.AssertjUtils;
import guru.mikelue.misc.lang.data.BagDataFactory;
import guru.mikelue.misc.lang.tuple.Tuple;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.HibernateException;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;
import org.jeasy.random.randomizers.LastNameRandomizer;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mockit.Expectations;
import mockit.Mocked;

public class PostgresArrayUserTypeTest {
	private Logger logger = LoggerFactory.getLogger(PostgresArrayUserTypeTest.class);

	private UserType testedUserType = new PostgresArrayUserType();
	private final static LastNameRandomizer stringRandomizer = new LastNameRandomizer();
	private final static IntegerRangeRandomizer intRandomizer = new IntegerRangeRandomizer(3000, 4000);

	public PostgresArrayUserTypeTest() {}

	/**
	 * Tests the conversion of data from JDBC's array
	 */
	@ParameterizedTest
	@MethodSource
	void nullSafeGet(
		Object jdbcValue, Object expectedValue,
		@Mocked ResultSet mockRs, @Mocked java.sql.Array mockSqlArray
	) throws SQLException {
		final var fakeNames = new String[] { "fake_col_1" };

		new Expectations() {{
			mockRs.getArray(fakeNames[0]);
			result = expectedValue != null ? mockSqlArray : null;

			mockSqlArray.getArray();
			result = jdbcValue;
			times = expectedValue != null ? 1 : 0;
		}};

		var testedUserType = initUserType(expectedValue);

		if (jdbcValue != null) {
			logger.debug("From JDBC[{}] to Java[{}]", jdbcValue.getClass().getTypeName(), expectedValue.getClass().getTypeName());
		} else {
			logger.debug("From JDBC \"null\" to Java \"null\"");
		}

		var testedValue = testedUserType.nullSafeGet(mockRs, fakeNames, null, null);

		assertThat(testedValue).isEqualTo(expectedValue);
	}
	static Stream<Arguments> nullSafeGet()
	{
		final Object[] valuesBySupportedTypesOfJdbcDriver = {
			BagDataFactory.Arrays.buildShorts(1, i -> intRandomizer.getRandomValue().shortValue()),
			BagDataFactory.Arrays.buildInts(1, i -> intRandomizer.getRandomValue()),
			BagDataFactory.Arrays.buildLongs(1, i -> intRandomizer.getRandomValue().longValue()),
			BagDataFactory.Arrays.buildFloats(1, i -> intRandomizer.getRandomValue().floatValue()),
			BagDataFactory.Arrays.buildDoubles(1, i -> intRandomizer.getRandomValue().doubleValue()),
			BagDataFactory.Arrays.buildBooleans(1, i -> intRandomizer.getRandomValue() % 2 == 0),
			BagDataFactory.buildArray(1, i -> stringRandomizer.getRandomValue(), ArrayUtils.EMPTY_STRING_ARRAY)
		};

		// e.g., Integer[] -> Integer[]
		var sameTypeCases = Stream.<Object>of(valuesBySupportedTypesOfJdbcDriver)
			.<Tuple.Pair<Object, Object>>map(
				jdbcValue -> Tuple.of(jdbcValue, jdbcValue)
			);
		// e.g., Integer[] -> int[]
		var primitiveTypeCases = Stream.<Object>of(valuesBySupportedTypesOfJdbcDriver)
			.<Tuple.Pair<Object, Object>>map(
				jdbcValue -> Tuple.of(jdbcValue, ArrayUtils.toPrimitive(jdbcValue))
			);
		var nullCase = Stream.<Tuple.Pair<Object, Object>>of(
			Tuple.of(null, null)
		);

		return Stream.of(sameTypeCases, primitiveTypeCases, nullCase)
			.flatMap(UnaryOperator.identity())
			.map(
				t ->  arguments(t.getValue0(), t.getValue1())
			);
	}
	/**
	 * Tests the un-supported type for target value in Java.
	 */
	@Test
	void nullSafeGetForUnsupportedTypes(
		@Mocked ResultSet mockRs, @Mocked java.sql.Array mockSqlArray
	) throws SQLException {
		final var fakeNames = new String[] { "fake_col_2" };

		new Expectations() {{
			mockRs.getArray(fakeNames[0]);
			result = mockSqlArray;

			mockSqlArray.getArray();
			result = new Integer[] { 10, 20 };
		}};

		var testedUserType = initUserType(new RottenBanana[] { new RottenBanana() });

		assertThatThrownBy(
			AssertjUtils.asThrowingCallable(
				() -> testedUserType.nullSafeGet(mockRs, fakeNames, null, null)
			)
		)
			.isInstanceOf(HibernateException.class)
			.hasMessageContaining("Unable to convert value of type");
	}

	/**
	 * Tests the setting of array value to parameters of JDBC statements.
	 */
	@ParameterizedTest
	@MethodSource
	void nullSafeSet(
		Object fieldValue, Object paramValue,
		@Mocked PreparedStatement mockSt
	) throws SQLException {
		var testedUserType = initUserType(fieldValue);

		new Expectations() {{
			mockSt.setObject(1, paramValue, testedUserType.sqlTypes()[0]);
			times = fieldValue != null ? 1 : 0;

			mockSt.setArray(1, null);
			times = fieldValue == null ? 1 : 0;
		}};

		testedUserType.nullSafeSet(mockSt, paramValue, 1, null);
	}
	static Stream<Arguments> nullSafeSet()
	{
		final Object[] valuesOfObjects = {
			BagDataFactory.Arrays.buildShortsObj(1, i -> intRandomizer.getRandomValue().shortValue()),
			BagDataFactory.Arrays.buildIntsObj(1, i -> intRandomizer.getRandomValue()),
			BagDataFactory.Arrays.buildLongsObj(1, i -> intRandomizer.getRandomValue().longValue()),
			BagDataFactory.Arrays.buildFloatsObj(1, i -> intRandomizer.getRandomValue().floatValue()),
			BagDataFactory.Arrays.buildDoublesObj(1, i -> intRandomizer.getRandomValue().doubleValue()),
			BagDataFactory.Arrays.buildBooleansObj(1, i -> intRandomizer.getRandomValue() % 2 == 0),
			BagDataFactory.buildArray(1, i -> stringRandomizer.getRandomValue(), ArrayUtils.EMPTY_STRING_ARRAY)
		};

		// e.g., int[] -> int[]
		var sameTypeCases = Stream.<Object>of(valuesOfObjects)
			.map(
				value -> Boolean.class.equals(value.getClass().getComponentType()) ?
					ArrayUtils.toPrimitive((Boolean[])value) :
					ArrayUtils.toPrimitive(value)
			)
			.<Tuple.Pair<Object, Object>>map(
				sampleValue -> Tuple.of(sampleValue, sampleValue)
			);
		// e.g., Integer[] -> int[]
		var objectTypeCases = Stream.<Object>of(valuesOfObjects)
			.<Tuple.Pair<Object, Object>>map(
				sampleValue -> Tuple.of(sampleValue, ArrayUtils.toPrimitive(sampleValue))
			);
		var nullCase = Stream.<Tuple.Pair<Object, Object>>of(
			Tuple.of(null, null)
		);

		return Stream.of(
			sameTypeCases, objectTypeCases, nullCase
		)
			.flatMap(UnaryOperator.identity())
			.map(
				tuple -> arguments(tuple.getValue0(), tuple.getValue1())
			);
	}

	/**
	 * Tests the un-supported types for setting parameters of statements.
	 */
	@Test
	void nullSafeSetForUnsupportedTypes(
		@Mocked PreparedStatement mockSt
	) {
		var sampleValue = new RottenBanana[0];
		var testedUserType = initUserType(sampleValue);

		assertThatThrownBy(
			AssertjUtils.asThrowingCallable(
				() -> testedUserType.nullSafeSet(mockSt, sampleValue, 1, null)
			)
		)
			.isInstanceOf(HibernateException.class)
			.hasMessageContaining("Doesn't support type");
	}

	/**
	 * Tests disassemble of object.
	 */
	@ParameterizedTest
	@MethodSource
	void disassemble(Object sampleArray)
	{
		var serializableObject = testedUserType.disassemble(sampleArray);

		assertThat(serializableObject)
			.isEqualTo(testedUserType.assemble(serializableObject, null));
	}
	static Arguments[] disassemble()
	{
		return new Arguments[] {
			arguments(
				BagDataFactory.Arrays.buildInts(
					2, i -> intRandomizer.getRandomValue()
				)
			),
			arguments(
				(Object)BagDataFactory.Arrays.buildIntsObj(
					2, i -> intRandomizer.getRandomValue()
				)
			),
			arguments(
				(Object)BagDataFactory.buildArray(
					2, i -> stringRandomizer.getRandomValue(),
					ArrayUtils.EMPTY_STRING_ARRAY
				)
			),
			arguments( (Object)null ),
		};
	}

	/**
	 * Tests arrays for deep copy
	 */
	@ParameterizedTest
	@MethodSource
	void deepCopy(Object sampleArray, Object changedValue)
	{
		if (sampleArray == null) {
			assertThat(testedUserType.deepCopy(sampleArray))
				.isNull();
			return;
		}

		var testedResult = testedUserType.deepCopy(sampleArray);
		assertThat(testedResult).isEqualTo(sampleArray);

		/**
		 * Asserts that the new object is a cloned version
		 */
		Array.set(testedResult, 0, changedValue);
		assertThat(testedResult).isNotEqualTo(sampleArray);
		// :~)
	}
	static Arguments[] deepCopy()
	{
		return new Arguments[] {
			arguments( new int[] { 77, 88 }, 75 ),
			arguments( (Object)new Integer[] { 21, 37 }, 31 ),
			arguments( (Object)new String[] { "C1", "C2" }, "C10" ),
			arguments( null, null ),
		};
	}

	/**
	 * Tests the comparison of values by this user type.
	 */
	@ParameterizedTest
	@MethodSource
	void equals(Object a, Object b, boolean expectedResult)
	{
		assertThat(testedUserType.equals(a, b))
			.isEqualTo(expectedResult);
	}
	static Arguments[] equals()
	{
		return new Arguments[] {
			arguments(new int[] { 1, 2 }, new int[] { 1, 2 }, true),
			arguments(new int[] { 1, 2 }, new int[] { 1, 3 }, false),
			arguments(new int[] { 1, 2 }, null, false),
			arguments(null, new int[] { 1, 3 }, false),
			arguments(null, null, true),
		};
	}

	/**
	 * Tests the getting of hash code for arrays of different types
	 */
	@ParameterizedTest
	@MethodSource
	void hashCodeValue(Object sampleArray, ThrowingConsumer<Integer> assertion)
		throws Throwable
	{
		var testedUserType = initUserType(sampleArray);
		assertion.accept(testedUserType.hashCode(sampleArray));
	}
	static Stream<Arguments> hashCodeValue()
	{
		var nullCase = Stream.<Tuple.Pair<Object, ThrowingConsumer<Integer>>>of(
			Tuple.of(null, v -> assertThat(v).isEqualTo(0))
		);

		var valueCases = Stream.<Object>of(
				BagDataFactory.Arrays.buildShorts(1, i -> intRandomizer.getRandomValue().shortValue()),
				BagDataFactory.Arrays.buildShortsObj(1, i -> intRandomizer.getRandomValue().shortValue()),
				BagDataFactory.Arrays.buildInts(1, i -> intRandomizer.getRandomValue()),
				BagDataFactory.Arrays.buildIntsObj(1, i -> intRandomizer.getRandomValue()),
				BagDataFactory.Arrays.buildLongs(1, i -> intRandomizer.getRandomValue().longValue()),
				BagDataFactory.Arrays.buildLongsObj(1, i -> intRandomizer.getRandomValue().longValue()),
				BagDataFactory.Arrays.buildFloats(1, i -> intRandomizer.getRandomValue().floatValue()),
				BagDataFactory.Arrays.buildFloatsObj(1, i -> intRandomizer.getRandomValue().floatValue()),
				BagDataFactory.Arrays.buildDoubles(1, i -> intRandomizer.getRandomValue().doubleValue()),
				BagDataFactory.Arrays.buildDoublesObj(1, i -> intRandomizer.getRandomValue().doubleValue()),
				BagDataFactory.Arrays.buildBooleans(1, i -> intRandomizer.getRandomValue() % 2 == 0),
				BagDataFactory.Arrays.buildBooleansObj(1, i -> intRandomizer.getRandomValue() % 2 == 0),
				BagDataFactory.buildArray(1, i -> stringRandomizer.getRandomValue(), ArrayUtils.EMPTY_STRING_ARRAY)
			)
				.<Tuple.Pair<Object, ThrowingConsumer<Integer>>>map(
					data -> Tuple.of(
						data,
						v -> assertThat(v).isNotEqualTo(0)
					)
				);

		return Stream.of(nullCase, valueCases)
			.flatMap(UnaryOperator.identity())
			.map(
				t -> arguments(t.getValue0(), t.getValue1())
			);
	}

	private <T> UserType initUserType(T sampleValue)
	{
		var targetClass = sampleValue != null ?
			sampleValue.getClass() : int[].class;

		/**
		 * Sets-up the detected type of mapped field for ORM
		 */
		var properties = new Properties();
		properties.put(
			DynamicParameterizedType.RETURNED_CLASS,
			targetClass.getName()
		);
		// :~)

		var testedUserType = new PostgresArrayUserType();
		testedUserType.setParameterValues(properties);

		return testedUserType;
	}
}

class RottenBanana {}
