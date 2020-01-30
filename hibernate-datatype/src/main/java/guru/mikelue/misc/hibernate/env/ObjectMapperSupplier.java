package guru.mikelue.misc.hibernate.env;

import java.util.function.Supplier;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperSupplier implements Supplier<ObjectMapper> {
	private final static ObjectMapper objectMapper = new ObjectMapper();

	public ObjectMapperSupplier() {}

	@Override
	public ObjectMapper get()
	{
		return objectMapper;
	}
}
