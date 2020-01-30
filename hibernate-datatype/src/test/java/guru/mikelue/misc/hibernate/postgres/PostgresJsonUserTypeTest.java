package guru.mikelue.misc.hibernate.postgres;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.stream.Stream;

import guru.mikelue.misc.lang.tuple.Tuple;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;

public class PostgresJsonUserTypeTest {
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(PostgresJsonUserTypeTest.class);

	public PostgresJsonUserTypeTest() {}

	@Tested
	private PostgresJsonUserType testedUserType;
	@Injectable
	private Supplier<ObjectMapper> objectMapperSupplier;

	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Tests the getting of hash code
	 */
	@ParameterizedTest
	@MethodSource
	void hashCodeValue(Object sampleObject, int expectedValue)
	{
		assertThat(testedUserType.hashCode(sampleObject))
			.isEqualTo(expectedValue);
	}
	static Arguments[] hashCodeValue()
	{
		return new Arguments[] {
			arguments(new FlyKoala("Kake", 10), -837961890),
			arguments(null, 0),
		};
	}

	/**
	 * Tests Assemble/Disassemble for object by JSON.
	 */
	@Test
	void disassembleAndAssemble()
	{
		initMockEnv();

		var sampleObject = new FlyKoala("John", 3);
		var testedObject = testedUserType.assemble(
			testedUserType.disassemble(sampleObject), null
		);

		assertThat(testedObject)
			.isEqualToComparingFieldByField(sampleObject);
	}

	/**
	 * Tests deep copy for the value of this user type.
	 */
	@Test
	void deepCopy()
	{
		initMockEnv();

		var sampleObject = new FlyKoala("Bob", 3);
		var testedObject = (FlyKoala)testedUserType.deepCopy(sampleObject);

		testedObject.name = "Cola";
		assertThat(testedObject.name)
			.isNotEqualTo(sampleObject.name);
	}

	/**
	 * Tests the getting of value from ResultSets.
	 */
	@ParameterizedTest
	@MethodSource
	void nullSafeGet(
		String sampleJson, ThrowingConsumer<ObjectAssert<?>> assertion,
		@Mocked ResultSet mockRs
	) throws Throwable {
		initMockEnv();

		var columns = new String[] { "col_1" };

		new Expectations() {{
			mockRs.getString(columns[0]);
			result = sampleJson;
		}};

		var testedValue = testedUserType.nullSafeGet(
			mockRs, columns, null, null
		);

		assertion.accept(assertThat(testedValue));
	}
	static Stream<Arguments> nullSafeGet()
	{
		var viableCases = Stream.<Tuple.Pair<String, FlyKoala>>of(
			Tuple.of("{ \"name\": \"joy\", \"age\": 30 }", new FlyKoala("joy", 30)),
			Tuple.of("{ \"name\": null, \"age\": 70 }", new FlyKoala(null, 70))
		)
			.map(
				t -> arguments(
					t.getValue0(),
					(ThrowingConsumer<ObjectAssert<FlyKoala>>) assertion ->
						assertion.isEqualToComparingFieldByField(t.getValue1())
				)
			);

		var nullCase = Stream.<Arguments>of(
			arguments(null, (ThrowingConsumer<ObjectAssert<FlyKoala>>) assertion -> assertion.isNull())
		);

		return Stream.of(viableCases, nullCase)
			.flatMap(s -> s);
	}

	/**
	 * Tests the setting of value to statments.
	 */
	@ParameterizedTest
	@MethodSource
	void nullSafeSet(
		Object sampleValue,
		@Mocked PreparedStatement mockSt
	) throws SQLException {
		initJsonUserType(testedUserType);

		new Expectations() {{
			mockSt.setObject(1, withInstanceOf(JsonNode.class), Types.OTHER);
			times = sampleValue != null ? 1 : 0;

			mockSt.setObject(1, null, Types.OTHER);
			times = sampleValue == null ? 1 : 0;
		}};

		testedUserType.nullSafeSet(mockSt, sampleValue, 1, null);
	}
	static Arguments[] nullSafeSet()
	{
		return new Arguments[] {
			arguments(new FlyKoala("Key", 33)),
			arguments((Object)null),
		};
	}

	private void initJsonUserType(PostgresJsonUserType testedUserType)
	{
		var props = new Properties();
		props.put(PostgresJsonUserType.RETURNED_CLASS, FlyKoala.class.getName());

		testedUserType.setParameterValues(props);
		testedUserType.setObjectMapperSupplier(objectMapperSupplier);
	}
	private void initMockEnv()
	{
		new Expectations(testedUserType) {{
			objectMapperSupplier.get();
			result = objectMapper;
			minTimes = 0;

			testedUserType.returnedClass();
			result = FlyKoala.class;
			minTimes = 0;
		}};
	}
}

class FlyKoala {
	@JsonProperty
	String name;
	@JsonProperty
	int age;

	FlyKoala() {}
	FlyKoala(String name, int age)
	{
		this.name = name;
		this.age = age;
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(100391, 22027)
			.append(name)
			.append(age)
		.toHashCode();
	}
}
