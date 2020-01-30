package guru.mikelue.misc.hibernate.postgres;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.assertj.core.api.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import guru.mikelue.misc.lang.agent.StringAgent;

import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mockit.Expectations;
import mockit.Mocked;

public class PostgresEnumUserTypeTest {
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(PostgresEnumUserTypeTest.class);

	private UserType testedUserType = initUserType();

	public PostgresEnumUserTypeTest() {}

	/**
	 * Tests the setting parameters to {@link PreparedStatement}.
	 */
	@ParameterizedTest
	@MethodSource
	void nullSafeSet(
		FootType sampleValue,
		@Mocked PreparedStatement mockSt
	) throws SQLException {
		new Expectations() {{
			mockSt.setObject(
				1,
				sampleValue != null ? sampleValue.value() : null,
				Types.OTHER
			);
			times = 1;
		}};

		testedUserType.nullSafeSet(mockSt, sampleValue, 1, null);
	}
	static Arguments[] nullSafeSet()
	{
		return new Arguments[] {
			arguments(FootType.Hippo),
			arguments((FootType)null),
		};
	}

	/**
	 * Tests the getting of field value from {@link ResultSet}.
	 */
	@ParameterizedTest
	@MethodSource
	void nullSafeGet(
		FootType sampleValue,
		@Mocked ResultSet mockRs
	) throws SQLException {
		var columns = new String[] { "col_1" };

		new Expectations() {{
			mockRs.getString(columns[0]);
			result = sampleValue.value();
		}};

		var testedValue = testedUserType.nullSafeGet(mockRs, columns, null, null);

		assertThat(testedValue).isEqualTo(sampleValue);
	}
	static Arguments[] nullSafeGet()
	{
		return new Arguments[] {
			arguments(FootType.Hippo),
			arguments(FootType.No1Tiger),
		};
	}

	private static PostgresEnumUserType<FootType> initUserType()
	{
		/**
		 * Sets-up the detected type of mapped field for ORM
		 */
		var properties = new Properties();
		properties.put(
			DynamicParameterizedType.RETURNED_CLASS,
			FootType.class.getName()
		);
		// :~)

		var testedUserType = new PostgresEnumUserType<FootType>();
		testedUserType.setParameterValues(properties);

		return testedUserType;
	}
}

enum FootType implements StringAgent {
	Hippo, Zebra, No1Tiger("No1 Tiger");

	private final String stringValue;
	FootType()
	{
		this.stringValue = name();
	}
	FootType(String stringValue)
	{
		this.stringValue = stringValue;
	}

	@Override
	public String value()
	{
		return stringValue;
	}
}
