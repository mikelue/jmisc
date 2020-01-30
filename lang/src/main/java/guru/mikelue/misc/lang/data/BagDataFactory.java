package guru.mikelue.misc.lang.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Provides methods to build collections(array, {@link List}, {@link Set}, or {@link Map})
 * from customized {@link IntFunction}.
 *
 * @see BagDataBuilder
 */
public interface BagDataFactory {
	/**
	 * Provides methods to build arrays for build-in types of Java
	 */
	public interface Arrays {
		public static Object[] buildObjects(int size, IntFunction<? extends Object> supplier)
		{
			return buildArray(size, supplier, ArrayUtils.EMPTY_OBJECT_ARRAY);
		}
		public static String[] buildStrings(int size, IntFunction<? extends String> supplier)
		{
			return buildArray(size, supplier, ArrayUtils.EMPTY_STRING_ARRAY);
		}
		public static Boolean[] buildBooleansObj(int size, IntFunction<? extends Boolean> supplier)
		{
			return buildArray(size, supplier, ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY);
		}
		public static boolean[] buildBooleans(int size, IntFunction<? extends Boolean> supplier)
		{
			return ArrayUtils.toPrimitive(buildBooleansObj(size, supplier));
		}
		public static Character[] buildCharsObj(int size, IntFunction<? extends Character> supplier)
		{
			return buildArray(size, supplier, ArrayUtils.EMPTY_CHARACTER_OBJECT_ARRAY);
		}
		public static char[] buildChars(int size, IntFunction<? extends Character> supplier)
		{
			return ArrayUtils.toPrimitive(buildCharsObj(size, supplier));
		}
		public static Byte[] buildBytesObj(int size, IntFunction<? extends Byte> supplier)
		{
			return buildArray(size, supplier, ArrayUtils.EMPTY_BYTE_OBJECT_ARRAY);
		}
		public static byte[] buildBytes(int size, IntFunction<? extends Byte> supplier)
		{
			return ArrayUtils.toPrimitive(buildBytesObj(size, supplier));
		}
		public static Short[] buildShortsObj(int size, IntFunction<? extends Short> supplier)
		{
			return buildArray(size, supplier, ArrayUtils.EMPTY_SHORT_OBJECT_ARRAY);
		}
		public static short[] buildShorts(int size, IntFunction<? extends Short> supplier)
		{
			return ArrayUtils.toPrimitive(buildShortsObj(size, supplier));
		}
		public static Integer[] buildIntsObj(int size, IntFunction<? extends Integer> supplier)
		{
			return buildArray(size, supplier, ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY);
		}
		public static int[] buildInts(int size, IntFunction<? extends Integer> supplier)
		{
			return ArrayUtils.toPrimitive(buildIntsObj(size, supplier));
		}
		public static Long[] buildLongsObj(int size, IntFunction<? extends Long> supplier)
		{
			return buildArray(size, supplier, ArrayUtils.EMPTY_LONG_OBJECT_ARRAY);
		}
		public static long[] buildLongs(int size, IntFunction<? extends Long> supplier)
		{
			return ArrayUtils.toPrimitive(buildLongsObj(size, supplier));
		}
		public static Float[] buildFloatsObj(int size, IntFunction<? extends Float> supplier)
		{
			return buildArray(size, supplier, ArrayUtils.EMPTY_FLOAT_OBJECT_ARRAY);
		}
		public static float[] buildFloats(int size, IntFunction<? extends Float> supplier)
		{
			return ArrayUtils.toPrimitive(buildFloatsObj(size, supplier));
		}
		public static Double[] buildDoublesObj(int size, IntFunction<? extends Double> supplier)
		{
			return buildArray(size, supplier, ArrayUtils.EMPTY_DOUBLE_OBJECT_ARRAY);
		}
		public static double[] buildDoubles(int size, IntFunction<? extends Double> supplier)
		{
			return ArrayUtils.toPrimitive(buildDoublesObj(size, supplier));
		}
	}

	/**
	 * Provides methods to construct a new object of {@link BagDataBuilder} for primitive types of Java
	 */
	public interface Builders {
		public static BagDataBuilder<Object> newObjectsForge(IntFunction<? extends Object> supplier)
		{
			return BagDataBuilder.of(Object.class, supplier);
		}
		public static BagDataBuilder<String> newStringsForge(IntFunction<? extends String> supplier)
		{
			return BagDataBuilder.of(String.class, supplier);
		}
		public static BagDataBuilder<Boolean> newBooleansObjForge(IntFunction<? extends Boolean> supplier)
		{
			return BagDataBuilder.of(Boolean.class, supplier);
		}
		public static BagDataBuilder<Character> newCharsObjForge(IntFunction<? extends Character> supplier)
		{
			return BagDataBuilder.of(Character.class, supplier);
		}
		public static BagDataBuilder<Byte> newBytesObjForge(IntFunction<? extends Byte> supplier)
		{
			return BagDataBuilder.of(Byte.class, supplier);
		}
		public static BagDataBuilder<Short> newShortsObjForge(IntFunction<? extends Short> supplier)
		{
			return BagDataBuilder.of(Short.class, supplier);
		}
		public static BagDataBuilder<Integer> newIntsObjForge(IntFunction<? extends Integer> supplier)
		{
			return BagDataBuilder.of(Integer.class, supplier);
		}
		public static BagDataBuilder<Long> newLongsForge(IntFunction<? extends Long> supplier)
		{
			return BagDataBuilder.of(Long.class, supplier);
		}
		public static BagDataBuilder<Float> newFloatsForge(IntFunction<? extends Float> supplier)
		{
			return BagDataBuilder.of(Float.class, supplier);
		}
		public static BagDataBuilder<Double> newDoublesForge(IntFunction<? extends Double> supplier)
		{
			return BagDataBuilder.of(Double.class, supplier);
		}
	}

	public static <T> T[] buildArray(int size, IntFunction<? extends T> supplier, T[] a)
	{
		var newList = buildList(size, supplier);
		return newList.toArray(a);
	}
	public static <T> T[] buildArrayOrNull(int size, IntFunction<? extends T> supplier, T[] a)
	{
		if (size <= 0) {
			return null;
		}

		return buildArray(size, supplier, a);
	}

	public static <T> List<T> buildList(int size, IntFunction<? extends T> supplier)
	{
		var newList = new ArrayList<T>();

		for (var i = 0; i < size; i++) {
			newList.add(supplier.apply(i));
		}

		return newList;
	}
	public static <T> List<T> buildListOrNull(int size, IntFunction<? extends T> supplier)
	{
		if (size <= 0) {
			return null;
		}

		return buildList(size, supplier);
	}

	public static <T> Set<T> buildSet(int size, IntFunction<? extends T> supplier)
	{
		var newSet = new HashSet<T>();

		for (var i = 0; i < size; i++) {
			newSet.add(supplier.apply(i));
		}

		return newSet;
	}
	public static <T> Set<T> buildSetOrNull(int size, IntFunction<? extends T> supplier)
	{
		if (size <= 0) {
			return null;
		}

		return buildSet(size, supplier);
	}

	public static <K, V> Map<K, V> buildMap(int size, IntFunction<? extends K> keySupplier, IntFunction<? extends V> valueSupplier)
	{
		var newMap = new HashMap<K, V>();

		for (var i = 0; i < size; i++) {
			newMap.put(keySupplier.apply(i), valueSupplier.apply(i));
		}

		return newMap;
	}
	public static <K, V> Map<K, V> buildMapOrNull(int size, IntFunction<? extends K> keySupplier, IntFunction<? extends V> valueSupplier)
	{
		if (size <= 0) {
			return null;
		}

		return buildMap(size, keySupplier, valueSupplier);
	}

	public static <T> Stream<T> buildStream(int size, IntFunction<? extends T> supplier)
	{
		return IntStream.range(0, size)
			.mapToObj(supplier);
	}
}
