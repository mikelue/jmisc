package guru.mikelue.misc.lang.tuple;

public interface KeyValue<K, V> extends Tuple.Pair<K, V> {
	default K getKey()
	{
		return getValue(0);
	}
	default V getValue()
	{
		return getValue(1);
	}
	default void setKey(K newKey)
	{
		setValue(0, newKey);
	}
	default void setValue(V newValue)
	{
		setValue(1, newValue);
	}

	public static <K, V> KeyValue<K, V> of(K key, V value)
	{
		return new KeyValueImpl<>(key, value);
	}

	@Override
	default KeyValue<K, V> typedClone() { return new KeyValueImpl<>((KeyValueImpl<K, V>)this); }

	static class KeyValueImpl<K, V> extends TupleImpl implements KeyValue<K, V> {
		KeyValueImpl(Object key, Object value)
		{
			super(new Object[] { key, value });
		}

		KeyValueImpl(KeyValueImpl<K, V> source) { super(source); }
	}
}
