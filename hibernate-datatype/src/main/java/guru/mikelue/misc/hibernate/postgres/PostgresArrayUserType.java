package guru.mikelue.misc.hibernate.postgres;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Postgres' JDBC supports arrays of primitive types feed to Statement.setObject():
 * <pre><code>
 * short[] 	int2[]
 * int[] 	int4[]
 * long[] 	int8[]
 * float[] 	float4[]
 * double[] 	float8[]
 * boolean[] 	bool[]
 * String[] 	varchar[]
 * </code></pre>
 *
 * The type of {@link ResultSet} provided by Postgres' JDBC is array of objects({@link Integer}, {@link String}, etc.).
 *
 * @see <a href="https://jdbc.postgresql.org/documentation/head/arrays.html">Array(Postgres JDBC)</a>
 */
public class PostgresArrayUserType implements UserType, DynamicParameterizedType {
	private final Logger logger = LoggerFactory.getLogger(PostgresArrayUserType.class);

	private Class<?> targetType;

	public PostgresArrayUserType() {}

	@Override
	public void setParameterValues(Properties prop)
	{
		var className = prop.getProperty(RETURNED_CLASS);
		try {
			targetType = Class.forName(className);
			logger.debug("Mapped array type of ORM: {}", targetType.getComponentType());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(String.format("Class of \"%s\" cannot be loaded.", className), e);
		}
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException
	{
		return cached;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException
	{
		if (value == null) {
			return null;
		}

		if (!Serializable.class.isInstance(value)) {
			throw new HibernateException(String.format("Type \"%\"s is not Serializable", value.getClass().getTypeName()));
		}

		return (Serializable)value;
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException
	{
		if (value == null) {
			return null;
		}

		var length = Array.getLength(value);
		var newArray = Array.newInstance(value.getClass().getComponentType(), length);

		System.arraycopy(value, 0, newArray, 0, length);

		return newArray;
	}

	@Override
	public boolean equals(Object a, Object b) throws HibernateException
	{
		return new EqualsBuilder()
			.append(a, b)
			.isEquals();
	}

	@Override
	public int hashCode(Object value) throws HibernateException
	{
		if (value == null) {
			return 0;
		}

		return ArrayUtils.hashCode(value);
	}

	@Override
	public boolean isMutable()
	{
		return true;
	}

	@Override
	public Object nullSafeGet(
		ResultSet rs, String[] names,
		SharedSessionContractImplementor session, Object owner
	) throws HibernateException, SQLException
	{
		var jdbcArrayImpl = rs.getArray(names[0]);

		if (jdbcArrayImpl == null) {
			return null;
		}

		var arrayValue = jdbcArrayImpl.getArray();

		if (targetType.isAssignableFrom(arrayValue.getClass())) {
			return arrayValue;
		}

		var convertedValue = ArrayUtils.toPrimitive(arrayValue);

		if (!targetType.isAssignableFrom(convertedValue.getClass())) {
			throw new HibernateException(String.format(
				"Unable to convert value of type[%s] from JDBC type[%s]",
				targetType.getTypeName(),
				convertedValue.getClass().getTypeName()
			));
		}

		return convertedValue;
	}

	@Override
	public void nullSafeSet(
		PreparedStatement st, Object value, int index,
		SharedSessionContractImplementor session
	) throws HibernateException, SQLException
	{
		if (value == null) {
			st.setArray(index, null);
			return;
		}

		var convertedValue = ArrayUtils.toPrimitive(value);
		if (!isSupportedJdbcType(convertedValue.getClass())) {
			throw new HibernateException(String.format(
				"Doesn't support type for setting parameters of statements: %s",
				convertedValue.getClass().getTypeName()
			));
		}

		st.setObject(index, convertedValue, Types.ARRAY);
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException
	{
		return deepCopy(original);
	}

	@Override
	public Class<?> returnedClass()
	{
		return targetType;
	}

	private final static int[] sqlTypesOfArray = new int[] { Types.ARRAY };
	@Override
	public int[] sqlTypes()
	{
		return sqlTypesOfArray;
	}

	private static final Set<String> supportedTypes = Set.of(
		short[].class.getTypeName(),
		int[].class.getTypeName(),
		long[].class.getTypeName(),
		float[].class.getTypeName(),
		double[].class.getTypeName(),
		boolean[].class.getTypeName(),
		String[].class.getTypeName()
	);
	private static boolean isSupportedJdbcType(Class<?> checkedType)
	{
		return supportedTypes.contains(checkedType.getTypeName());
	}
}
