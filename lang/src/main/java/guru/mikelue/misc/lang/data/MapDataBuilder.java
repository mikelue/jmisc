package guru.mikelue.misc.lang.data;

import java.util.Map;
import java.util.function.IntFunction;

/**
 * Reusable builder for {@link Map} object.
 */
public class MapDataBuilder<K, V> {
	public static <K, V> MapDataBuilder<K, V> of(IntFunction<? extends K> keySupplier, IntFunction<? extends V> valueSupplier)
	{
		return new MapDataBuilder<>(keySupplier, valueSupplier);
	}

	private final IntFunction<? extends K> keySupplier;
	private final IntFunction<? extends V> valueSupplier;

	private MapDataBuilder(IntFunction<? extends K> keySupplier, IntFunction<? extends V> valueSupplier)
	{
		this.keySupplier = keySupplier;
		this.valueSupplier = valueSupplier;
	}

	public Map<K, V> build(int size)
	{
		return BagDataFactory.buildMap(size, keySupplier, valueSupplier);
	}
	public Map<K, V> buildOrNull(int size)
	{
		return BagDataFactory.buildMapOrNull(size, keySupplier, valueSupplier);
	}
}
