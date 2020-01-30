package guru.mikelue.misc.lang.tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.math.NumberUtils;

import guru.mikelue.misc.lang.tuple.TupleImpl.*;

@SuppressWarnings("unchecked")
class TupleImpl implements Tuple {
	private final Object[] tupleData;

	private TupleImpl(int size)
	{
		tupleData = new Object[size];
	}

	protected TupleImpl(Object[] tupleData)
	{
		this.tupleData = tupleData;
	}

	/**
	 * Copy constructor
	 */
	TupleImpl(TupleImpl source)
	{
		tupleData = source.toArray();
	}

	<T extends Tuple> T addWithLimitSize(int limitSize, Tuple right)
	{
		return addWithLimitSize(limitSize, right, tupleData.length);
	}

	<T extends Tuple> T addWithLimitSize(int limitSize, Tuple right, int atPos)
	{
		var newTuple = TypedTupleFactory.newTupleBySize(limitSize);

		int length = 0;
		int selfIndex = 0;
		int newIndex = 0;

		/**
		 * First part of new Tuple(from elements before atPos of this Tuple)
		 */
		length = NumberUtils.min(limitSize, tupleData.length, atPos);
		System.arraycopy(tupleData, selfIndex, newTuple.tupleData, newIndex, length);
		selfIndex += length;
		newIndex += length;
		// :~)

		/**
		 * Adds elements of right Tuple
		 */
		var rightTupleData = ((TupleImpl)right).tupleData;
		length = NumberUtils.min(limitSize - newIndex, rightTupleData.length);
		System.arraycopy(rightTupleData, 0, newTuple.tupleData, newIndex, length);
		newIndex += length;
		// :~)

		/**
		 * Adds rest element of this Tuple
		 */
		length = NumberUtils.min(limitSize - newIndex, tupleData.length - selfIndex);
		System.arraycopy(tupleData, selfIndex, newTuple.tupleData, newIndex, length);
		// :~)

		return (T)newTuple;
	}

	/**
	 * Uses reflection of Java to get "clone-constructor" of sub-class.
	 */
	static <T extends Tuple> T cloneToType(T tuple)
	{
		return TypedTupleFactory.cloneTupleByType(tuple);
	}

	@Override
	public int getSize()
	{
		return tupleData.length;
	}
	@Override
	public <S> S getValue(int index)
	{
		assertIndex(index);
		return (S)tupleData[index];
	}
	@Override
	public <S> void setValue(int index, S newValue)
	{
		assertIndex(index);
		tupleData[index] = newValue;
	}

	@Override
	public Object[] toArray()
	{
		return Arrays.copyOf(tupleData, tupleData.length);
	}
	@Override
	public List<Object> toList()
	{
		var resultList = new ArrayList<Object>(tupleData.length);

		for (var ele: tupleData) {
			resultList.add(ele);
		}

		return resultList;
	}
	@Override
	public boolean contains(Object value)
	{
		if (value == null) {
			for (var v: tupleData) {
				if (v == null) {
					return true;
				}
			}
			return false;
		}

		for (var v: tupleData) {
			if (value.equals(v)) {
				return true;
			}
		}

		return false;
	}
	@Override
	public boolean containsAll(Object... rest)
	{
		for (var checked: rest) {
			if (!contains(checked)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public int indexOf(Object value)
	{
		if (value == null) {
			return indexOf(
				value, (a, b) -> b == null
			);
		}

		return indexOf(
			value, Object::equals
		);
	}
	@Override
	public int lastIndexOf(Object value)
	{
		if (value == null) {
			return lastIndexOf(
				value, (a, b) -> b == null
			);
		}

		return lastIndexOf(
			value, Object::equals
		);
	}

	@Override
	public TupleImpl clone()
	{
		return new TupleImpl(this);
	}

	private int indexOf(Object value, BiFunction<Object, Object, Boolean> compareFunction)
	{
		for (var i = 0; i < tupleData.length; i++) {
			if (compareFunction.apply(value, tupleData[i])) {
				return i;
			}
		}

		return -1;
	}
	private int lastIndexOf(Object value, BiFunction<Object, Object, Boolean> compareFunction)
	{
		for (var i = tupleData.length - 1; i >= 0; i--) {
			if (compareFunction.apply(value, tupleData[i])) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) { return false; }
		if (obj == this) { return true; }
		if (obj.getClass() != getClass()) {
			return false;
		}

		var rhs = (TupleImpl)obj;
		return new EqualsBuilder()
			.append(tupleData, rhs.tupleData)
			.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(25583, 249421)
			.append(tupleData)
		.toHashCode();
	}

	@Override
	public String toString()
	{
		var elementsText = new ArrayList<String>(tupleData.length);

		for (int i = 0; i < tupleData.length; i++) {
			elementsText.add(String.format("<[%d] \"%s\">", i, tupleData[i] == null ? "<null>" : tupleData[i].toString()));
		}

		return String.format(
			"Tuple[%d]: [ %s ]",
			tupleData.length,
			elementsText.stream()
				.collect(Collectors.joining(", "))
		);
	}

	private void assertIndex(int index)
	{
		if (index < 0 || index >= getSize()) {
			throw new IndexOutOfBoundsException(String.format(
				"Index for tuple out of bound: %d <%d>",
				index, getSize()
			));
		}
	}

	@Override
	public int compareTo(Object another)
	{
		var anotherImpl = (TupleImpl)another;

		return new CompareToBuilder()
			.append(tupleData, anotherImpl.tupleData)
			.toComparison();
	}

	@Override
	public Iterator<Object> iterator()
	{
		return toList().iterator();
	}

	static class UnitImpl<A> extends TupleImpl implements Unit<A> {
		UnitImpl(A v0)
		{
			super(new Object[] { v0 });
		}

		UnitImpl() { super(1); }
		UnitImpl(UnitImpl<A> source) { super(source); }
	}
	static class PairImpl<A, B> extends TupleImpl implements Pair<A, B> {
		PairImpl(A v0, B v1)
		{
			super(new Object[] { v0, v1 });
		}

		PairImpl() { super(2); }
		PairImpl(PairImpl<A, B> source) { super(source); }
	}
	static class TripletImpl<A, B, C> extends TupleImpl implements Triplet<A, B, C> {
		TripletImpl(A v0, B v1, C v2)
		{
			super(new Object[] { v0, v1, v2 });
		}

		TripletImpl() { super(3); }
		TripletImpl(TripletImpl<A, B, C> source) { super(source); }
	}
	static class QuartetImpl<A, B, C, D> extends TupleImpl implements Quartet<A, B, C, D> {
		QuartetImpl(A v0, B v1, C v2, D v3)
		{
			super(new Object[] { v0, v1, v2, v3 });
		}

		QuartetImpl() { super(4); }
		QuartetImpl(QuartetImpl<A, B, C, D> source) { super(source); }
	}
	static class QuintetImpl<A, B, C, D, E> extends TupleImpl implements Quintet<A, B, C, D, E> {
		QuintetImpl(A v0, B v1, C v2, D v3, E v4)
		{
			super(new Object[] { v0, v1, v2, v3, v4 });
		}

		QuintetImpl() { super(5); }
		QuintetImpl(QuintetImpl<A, B, C, D, E> source) { super(source); }
	}
	static class SextetImpl<A, B, C, D, E, F> extends TupleImpl implements Sextet<A, B, C, D, E, F> {
		SextetImpl(A v0, B v1, C v2, D v3, E v4, F v5)
		{
			super(new Object[] { v0, v1, v2, v3, v4, v5 });
		}

		SextetImpl() { super(6); }
		SextetImpl(SextetImpl<A, B, C, D, E, F> source) { super(source); }
	}
	static class SeptetImpl<A, B, C, D, E, F, G> extends TupleImpl implements Septet<A, B, C, D, E, F, G> {
		SeptetImpl(A v0, B v1, C v2, D v3, E v4, F v5, G v6)
		{
			super(new Object[] { v0, v1, v2, v3, v4, v5, v6 });
		}

		SeptetImpl() { super(7); }
		SeptetImpl(SeptetImpl<A, B, C, D, E, F, G> source) { super(source); }
	}
	static class OctetImpl<A, B, C, D, E, F, G, H> extends TupleImpl implements Octet<A, B, C, D, E, F, G, H> {
		OctetImpl(A v0, B v1, C v2, D v3, E v4, F v5, G v6, H v7)
		{
			super(new Object[] { v0, v1, v2, v3, v4, v5, v6, v7 });
		}

		OctetImpl() { super(8); }
		OctetImpl(OctetImpl<A, B, C, D, E, F, G, H> source) { super(source); }
	}
	static class EnneadImpl<A, B, C, D, E, F, G, H, I> extends TupleImpl implements Ennead<A, B, C, D, E, F, G, H, I> {
		EnneadImpl(A v0, B v1, C v2, D v3, E v4, F v5, G v6, H v7, I v8)
		{
			super(new Object[] { v0, v1, v2, v3, v4, v5, v6, v7, v8 });
		}

		EnneadImpl() { super(9); }
		EnneadImpl(EnneadImpl<A, B, C, D, E, F, G, H, I> source) { super(source); }
	}
	static class DecadeImpl<A, B, C, D, E, F, G, H, I, J> extends TupleImpl implements Decade<A, B, C, D, E, F, G, H, I, J> {
		DecadeImpl(A v0, B v1, C v2, D v3, E v4, F v5, G v6, H v7, I v8, J v9)
		{
			super(new Object[] { v0, v1, v2, v3, v4, v5, v6, v7, v8, v9 });
		}

		DecadeImpl() { super(10); }
		DecadeImpl(DecadeImpl<A, B, C, D, E, F, G, H, I, J> source) { super(source); }
	}
}

class TypedTupleFactory {
	@SuppressWarnings("unchecked")
	static <T extends Tuple> T cloneTupleByType(T source)
	{
		TupleImpl clonedInstance;

		switch (source.getClass().getSimpleName()) {
			case "UnitImpl":
				clonedInstance = new UnitImpl<>((UnitImpl<?>)source);
				break;
			case "PairImpl":
				clonedInstance = new PairImpl<>((PairImpl<?, ?>)source);
				break;
			case "TripletImpl":
				clonedInstance = new TripletImpl<>((TripletImpl<?, ?, ?>)source);
				break;
			case "QuartetImpl":
				clonedInstance = new QuartetImpl<>((QuartetImpl<?, ?, ?, ?>)source);
				break;
			case "QuintetImpl":
				clonedInstance = new QuintetImpl<>((QuintetImpl<?, ?, ?, ?, ?>)source);
				break;
			case "SextetImpl":
				clonedInstance = new SextetImpl<>((SextetImpl<?, ?, ?, ?, ?, ?>)source);
				break;
			case "SeptetImpl":
				clonedInstance = new SeptetImpl<>((SeptetImpl<?, ?, ?, ?, ?, ?, ?>)source);
				break;
			case "OctetImpl":
				clonedInstance = new OctetImpl<>((OctetImpl<?, ?, ?, ?, ?, ?, ?, ?>)source);
				break;
			case "EnneadImpl":
				clonedInstance = new EnneadImpl<>((EnneadImpl<?, ?, ?, ?, ?, ?, ?, ?, ?>)source);
				break;
			case "DecadeImpl":
				clonedInstance = new DecadeImpl<>((DecadeImpl<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>)source);
				break;
			default:
				throw new RuntimeException(String.format("Cannot figure out clone constructor for \"%s\"", source.getClass().getTypeName()));
		}

		return (T)clonedInstance;
	}
	static TupleImpl newTupleBySize(int size)
	{
		switch (size) {
			case 1:
				return new UnitImpl<>();
			case 2:
				return new PairImpl<>();
			case 3:
				return new TripletImpl<>();
			case 4:
				return new QuartetImpl<>();
			case 5:
				return new QuintetImpl<>();
			case 6:
				return new SextetImpl<>();
			case 7:
				return new SeptetImpl<>();
			case 8:
				return new OctetImpl<>();
			case 9:
				return new EnneadImpl<>();
			case 10:
				return new DecadeImpl<>();
		}

		throw new RuntimeException(String.format("Unsupported size[%d] for new tuple", size));
	}
}
