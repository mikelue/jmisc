package guru.mikelue.misc.lang.data;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface BagDataBuilderBase<T> {
	public T[] buildArray(int size);
	public default T[] buildArrayOrNull(int size)
	{
		return size > 0 ? buildArray(size) : null;
	}
	public List<T> buildList(int size);
	public default List<T> buildListOrNull(int size)
	{
		return size > 0 ? buildList(size) : null;
	}
	public Set<T> buildSet(int size);
	public default Set<T> buildSetOrNull(int size)
	{
		return size > 0 ? buildSet(size) : null;
	}
	public default Stream<T> buildStream(int size)
	{
		return buildList(size).stream();
	}
}
