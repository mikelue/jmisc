package guru.mikelue.misc.hibernate.postgres;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import guru.mikelue.misc.lang.agent.StringAgent;

import org.hibernate.HibernateException;
import org.hibernate.TypeMismatchException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.EnumType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class PostgresEnumUserType<T extends Enum<T> & StringAgent> extends EnumType<T> {
	private final Logger logger = LoggerFactory.getLogger(PostgresEnumUserType.class);

	private StringAgent.EnumMate<T> stringCodeMapping;
	private Class<T> typeOfEnum;

	public PostgresEnumUserType() {}

	@Override
	public Class<T> returnedClass()
	{
		return typeOfEnum;
	}

	private final static int[] sqlTypesOfEnum = new int[] { Types.OTHER };
	@Override
	public int[] sqlTypes()
	{
		return sqlTypesOfEnum;
	}

	@Override
	public void nullSafeSet(
		PreparedStatement st, Object value, int index,
		SharedSessionContractImplementor session
	) throws HibernateException, SQLException {
		var stringAgent = (StringAgent)value;

		st.setObject(index, stringAgent == null ? null : stringAgent.value(), sqlTypesOfEnum[0]);
	}

	@Override
	public Enum<?> nullSafeGet(
		ResultSet rs, String[] names,
		SharedSessionContractImplementor session, Object owner
	) throws SQLException
	{
		var stringValue = rs.getString(names[0]);

		if (stringValue == null) {
			return null;
		}

		return stringCodeMapping.getEnumOptional(stringValue)
			.orElseThrow(
				() -> new SQLException(String.format(
					 "Cannot get enum instance by string code: \"%s\". Type of enum: %s", stringValue, typeOfEnum
				 ))
			);
	}

	@Override @SuppressWarnings("unchecked")
	public void setParameterValues(Properties parameters)
	{
		var targetEnumTypeName = parameters.getProperty(RETURNED_CLASS);
		logger.debug("Target type of enum: {}", targetEnumTypeName);

		Class<?> returnedClass;
		try {
			returnedClass = Class.forName(targetEnumTypeName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		if (!StringAgent.class.isAssignableFrom(returnedClass)) {
			throw new TypeMismatchException(String.format("Type \"%s\" needs to implement StringAgent", returnedClass));
		}

		typeOfEnum = (Class<T>)returnedClass;
		stringCodeMapping = StringAgent.asEnumMate(typeOfEnum);

		logger.debug("Init enum type as Postgres's enum: {}", typeOfEnum.getTypeName());
	}
}
