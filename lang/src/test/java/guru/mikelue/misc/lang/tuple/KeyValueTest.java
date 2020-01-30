package guru.mikelue.misc.lang.tuple;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.*;

public class KeyValueTest {
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(KeyValueTest.class);

	public KeyValueTest() {}

	/**
	 * Tests the play of KeyValue.
	 */
	@Test
	void getKeyValue()
	{
		var testedKeyValue = KeyValue.of("key", 99);

		assertThat(testedKeyValue.getKey())
			.isEqualTo("key");
		assertThat(testedKeyValue.getValue())
			.isEqualTo(99);
	}

	/**
	 * Tests the clone method of typed.
	 */
	@Test
	void typedClone()
	{
		var source = KeyValue.of("g1", 40);
		var clonedOne = source.typedClone();

		clonedOne.setValue(30);

		assertThat(source.getValue()).isEqualTo(40);
		assertThat(clonedOne.getValue()).isEqualTo(30);
	}
}
