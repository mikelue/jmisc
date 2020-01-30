package guru.mikelue.misc.hibernate.postgres;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;
import org.hibernate.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  This user type maps between Postgres' JSON and Jackson JSON.
 *
 *  The types of field in entity supported any type which could be serialized by Jackson JSON engine.
 *
 *  The types of Postgres' JSON supports "json" and "jsonb"(default).
 *
 *  You can put {@link Parameter} to set-up text json as type of Postgres' database.
 *
 * 	Example:
 *  <pre><code>
 *  @TypeDefs({
 *  	@TypeDef(
 *  		name = "postgres-json",
 *  		typeClass = PostgresJsonUserType.class
 *  	)
 *  })
 *  @Entity
 *  public class SampleCar {
 *		// Saved as JSON in Postgres
 *		// Default storage type is JSON_STORAGE_BINARY
 *		@Type(type="postgres-json, parameters={@Parameter(name=JsonStorage.PARAM, value=JsonStorage.TEXT)})
 *  	private Model model;
 *  }
 *  </code></pre>
 */
public class PostgresJsonUserType implements UserType, DynamicParameterizedType {
	/**
	 *  Constants used in {@link Parameter} of Hibernate.
	 */
	public interface JsonStorage {
		public final static String PARAM = "_postgres_json_storage_type_";
		public final static String BINARY = "Binary";
		public final static String TEXT = "Text";
	}

	private final static String JACKSON_OBJECT_MAPPER_SUPPLIER_PROPERTY = "hibernate.jackson.object_mapper_supplier";

	private enum JsonStorageType {
		Text, Binary;
	}

	private final Logger logger = LoggerFactory.getLogger(PostgresJsonUserType.class);

	private Class<?> targetType;
	private NullSafeSetForJson nullSafeSetForJson;
	private Supplier<ObjectMapper> objectMapperSupplier;

	public PostgresJsonUserType() {}

	@Override
	public void setParameterValues(Properties prop)
	{
		/**
		 * Constructs object mapper
		 */
		var properties = Environment.getProperties();
		if (!properties.containsKey(JACKSON_OBJECT_MAPPER_SUPPLIER_PROPERTY)) {
			throw new HibernateException(String.format("Needs property \"%s\" as type of (Supplier<ObjectMapper>)", JACKSON_OBJECT_MAPPER_SUPPLIER_PROPERTY));
		}

		initObjectMapper(properties.getProperty(JACKSON_OBJECT_MAPPER_SUPPLIER_PROPERTY));
		// :~)

		/**
		 * Loads target type of JSON de-serialization
		 */
		try {
			targetType = Class.forName(
				prop.getProperty(RETURNED_CLASS)
			);
			logger.debug("Mapped type of ORM: {}", targetType);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		// :~)

		/**
		 * Decides storage type of JSON
		 */
		var storageType = JsonStorageType.valueOf(
			prop.getProperty(JsonStorage.PARAM, JsonStorageType.Binary.name())
		);

		switch (storageType) {
			case Text:
				nullSafeSetForJson = this::nullSafeSetForTextJson;
				break;
			case Binary:
				nullSafeSetForJson = this::nullSafeSetForBinaryJson;
				break;
		}
		// :~)

		logger.debug("JSON storage: {}", storageType);
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException
	{
		var objectClass = returnedClass();

		return workOnJson(
			objectMapper -> objectMapper.readValue((byte[])cached, objectClass),
			() -> String.format("Could not deserialize JSON to object of \"%s\".", objectClass.getTypeName())
		);
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException
	{
		return workOnJson(
			objectMapper -> objectMapper.writeValueAsBytes(value),
			() -> String.format("Could not serialize object[%s] to JSON.", value)
		);
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException
	{
		var targetType = returnedClass();

		return workOnJson(
			objectMapper -> objectMapper.readValue(
				objectMapper.writeValueAsBytes(value),
				targetType
			),
			() -> String.format("Could deep copy(by JSON) of object: [%s]", value)
		);
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

		return new HashCodeBuilder()
			.append(value)
			.toHashCode();
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public Object nullSafeGet(
		ResultSet rs, String[] names,
		SharedSessionContractImplementor session, Object owner
	) throws HibernateException, SQLException
	{
		var jsonText = rs.getString(names[0]);
		var targetClass = returnedClass();

		if (jsonText == null) {
			return null;
		}

		return workOnJson(
			objectMapper -> objectMapper.readValue(jsonText, targetClass),
			() -> String.format("Could not process JSON data of Postgres. Type: \"%s\".", targetClass.getTypeName())
		);
	}

	@Override
	public void nullSafeSet(
		PreparedStatement st, Object value, int index,
		SharedSessionContractImplementor session
	) throws HibernateException, SQLException
	{
		nullSafeSetForJson.doSet(st, value, index);
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

	// Used by @mockit.Injectable
	void setObjectMapperSupplier(Supplier<ObjectMapper> newSupplier)
	{
		objectMapperSupplier = newSupplier;
	}

	private final static int[] sqlTypesOfJson = new int[] { Types.OTHER };
	@Override
	public int[] sqlTypes()
	{
		return sqlTypesOfJson;
	}

	@SuppressWarnings("unchecked")
	private void initObjectMapper(String nameOfSupplierClass)
	{
		Class<Supplier<ObjectMapper>> supplierType;

		try {
			var loadedType = Class.forName(nameOfSupplierClass);

			if (Supplier.class.isInstance(loadedType)) {
				throw new HibernateException(String.format("Could not find \"%s\" is not type of Supplier", nameOfSupplierClass));
			}

			supplierType = (Class<Supplier<ObjectMapper>>)loadedType;
		} catch (ClassNotFoundException e) {
			throw new HibernateException(String.format("Could not find supplier of ObjectMapper: \"%s\"", nameOfSupplierClass), e);
		}
		logger.debug("Type of Supplier<ObjectMapper>: {}", supplierType.getTypeName());

		try {
			objectMapperSupplier = supplierType.getDeclaredConstructor()
				.newInstance();
		} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
			throw new HibernateException(String.format("Cannot instantiate type[%s]", supplierType.getTypeName()), e);
		}
		logger.debug("Instance of Supplier<ObjectMapper>: {}", objectMapperSupplier);
	}

	private void nullSafeSetForTextJson(PreparedStatement st, Object value, int index)
		throws SQLException
	{
		if (value == null) {
			st.setString(index, null);
			return;
		}

		var jsonRepresent = String.class.isInstance(value) ?
			(String)value :
			workOnJson(
				objectMapper -> objectMapper.writeValueAsString(value),
				() -> String.format("Could not convert object to JSON for: %s", value)
			);

		st.setString(index, jsonRepresent);
	}
	private void nullSafeSetForBinaryJson(PreparedStatement st, Object value, int index)
		throws SQLException
	{
		if (value == null) {
			st.setObject(index, null, sqlTypesOfJson[0]);
			return;
		}

		Object jsonRepresent = String.class.isInstance(value) ?
			value :
			workOnJson(
				objectMapper -> objectMapper.valueToTree(value),
				() -> String.format("Could not convert object to JSON for: %s", value)
			);

		st.setObject(index, jsonRepresent, sqlTypesOfJson[0]);
	}

	private <T> T workOnJson(JsonProcessor<T> jsonProcessor, Supplier<String> messageForException) throws HibernateException
	{
		var objectMapper = objectMapperSupplier.get();
		if (objectMapper == null) {
			throw new HibernateException("Cannot get viable instance of ObjectMapper");
		}

		try {
			return jsonProcessor.process(objectMapper);
		} catch (IOException e) {
			var message = messageForException.get();
			logger.error(message, e);
			throw new HibernateException(message, e);
		}
	}
}

@FunctionalInterface
interface JsonProcessor<T> {
	 T process(ObjectMapper objectMapper) throws IOException;
}
@FunctionalInterface
interface NullSafeSetForJson {
	 void doSet(PreparedStatement st, Object value, int index) throws SQLException;
}
