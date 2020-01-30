package guru.mikelue.misc.lang.data;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.*;

public class BagDataBuilderTest {
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(BagDataBuilderTest.class);

	public BagDataBuilderTest() {}

	/**
	 * Tests building of arrays.
	 */
	@Test
	void buildArray()
	{
		var testedBuilder = BagDataBuilder.of(
			Integer.class, i -> i * 2 + 1
		);

		assertThat(testedBuilder.buildArray(3))
			.contains(1, 3, 5);
	}

	/**
	 * Tests building of lists.
	 */
	@Test
	void buildList()
	{
		var testedBuilder = BagDataBuilder.of(
			Integer.class, i -> i * 2 + 11
		);

		assertThat(testedBuilder.buildList(3))
			.contains(11, 13, 15);
	}

	/**
	 * Tests building of sets.
	 */
	@Test
	void buildSet()
	{
		var testedBuilder = BagDataBuilder.of(
			Integer.class, i -> i * 2 + 21
		);

		assertThat(testedBuilder.buildSet(3))
			.contains(21, 23, 25);
	}

	/**
	 * Tests build of streams.
	 */
	@Test
	void buildStream()
	{
		var testedBuilder = BagDataBuilder.of(
			Integer.class, i -> i * 2 + 31
		);

		assertThat(testedBuilder.buildStream(3))
			.contains(31, 33, 35);
	}
}

class IntForgeTest {
	/**
	 * Tests the building of int array.
	 */
	@Test
	void buildIntArray()
	{
		var testedForge = BagDataBuilder.IntForge.of(i -> i * 2 + 1);

		assertThat(testedForge.buildArray(3))
			.contains(1, 3, 5);
	}

	/**
	 * Tests the building of int stream.
	 */
	@Test
	void buildIntStream()
	{
		var testedForge = BagDataBuilder.IntForge.of(i -> i * 2 + 11);

		assertThat(testedForge.buildStream(3))
			.contains(11, 13, 15);
	}
}

class LongForgeTest {
	/**
	 * Tests the building of int array.
	 */
	@Test
	void buildLongArray()
	{
		var testedForge = BagDataBuilder.LongForge.of(i -> i * 2 + 1);

		assertThat(testedForge.buildArray(3))
			.contains(1l, 3l, 5l);
	}

	/**
	 * Tests the building of int stream.
	 */
	@Test
	void buildLongStream()
	{
		var testedForge = BagDataBuilder.LongForge.of(i -> i * 2 + 11);

		assertThat(testedForge.buildStream(3))
			.contains(11l, 13l, 15l);
	}
}

class DoubleForgeTest {
	/**
	 * Tests the building of int array.
	 */
	@Test
	void buildDoubleArray()
	{
		var testedForge = BagDataBuilder.DoubleForge.of(i -> i * 2 + 1);

		assertThat(testedForge.buildArray(3))
			.contains(1d, 3d, 5d);
	}

	/**
	 * Tests the building of int stream.
	 */
	@Test
	void buildDoubleStream()
	{
		var testedForge = BagDataBuilder.DoubleForge.of(i -> i * 2 + 11);

		assertThat(testedForge.buildStream(3))
			.contains(11d, 13d, 15d);
	}
}
