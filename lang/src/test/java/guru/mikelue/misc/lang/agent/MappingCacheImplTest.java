package guru.mikelue.misc.lang.agent;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.*;

public class MappingCacheImplTest {
	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(MappingCacheImplTest.class);

	public MappingCacheImplTest() {}

	/**
	 * Tests building of map by value agent.
	 */
	@Test
	void buildMap()
	{
		var testedMap = MappingCacheImpl.buildMap(CarrotVariety.class);

		assertThat(testedMap)
			.containsEntry((byte)1, CarrotVariety.Danvers)
			.containsEntry((byte)2, CarrotVariety.Nantes)
			.containsEntry((byte)3, CarrotVariety.Imperator);
	}

	/**
	 * Tests the getting of cached mapping for value agent.
	 */
	@Test
	void getValueAgentMap()
	{
		var map1 = MappingCacheImpl.getMapOfValueAgent(CarrotVariety.class);
		var map2 = MappingCacheImpl.getMapOfValueAgent(CarrotVariety.class);

		assertThat(map1).isSameAs(map2);
	}

	/**
	 * Tests the building/caching of {@link ValueAgent#EnumMate}.
	 */
	@Test
	void getEnumMateOfValueAgent()
	{
		var mate1 = MappingCacheImpl.getEnumMateOfValueAgent(CarrotVariety.class);

		assertThat(mate1.getEnum((byte)1))
			.isEqualTo(CarrotVariety.Danvers);
		assertThat(mate1.getEnum((byte)2))
			.isEqualTo(CarrotVariety.Nantes);
		assertThat(mate1.getEnum((byte)3))
			.isEqualTo(CarrotVariety.Imperator);

		/**
		 * Asserts the optional getter
		 */
		assertThat(mate1.getEnumOptional((byte)10))
			.isNotPresent();
			// :~)

		/**
		 * Asserts the cache
		 */
		assertThat(
			MappingCacheImpl.getEnumMateOfValueAgent(CarrotVariety.class)
		)
			.isSameAs(mate1);
		// :~)
	}
}

enum CarrotVariety implements ValueAgent<Byte> {
	Danvers((byte)1), Nantes((byte)2), Imperator((byte)3);

	private final byte v;
	CarrotVariety(byte v)
	{
		this.v = v;
	}

	@Override
	public Byte value()
	{
		return v;
	}
}
