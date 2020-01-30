package guru.mikelue.misc.lang.tuple;

import java.util.List;

/**
 *  A mutable, type-safed structure.
 *
 * Typed tuples could be used to generate code skeleton:
 * <pre><code>
 * Unit<A>
 * Pair<A, B>
 * Triplet<A, B, C>
 * Quartet<A, B, C, D>
 * Quintet<A, B, C, D, E>
 * Sextet<A, B, C, D, E, F>
 * Septet<A, B, C, D, E, F, G>
 * Octet<A, B, C, D, E, F, G, H>
 * Ennead<A, B, C, D, E, F, G, H, I>
 * Decade<A, B, C, D, E, F, G, H, I, J>
 * </code></pre>
 *
 * @param <T> Any type implements {@link Tuple} self
 */
@SuppressWarnings("unchecked")
public interface Tuple extends Comparable<Object>, Iterable<Object>, Cloneable {
	public interface Unit<A> extends Tuple {
		default void setAt0(A new0)
		{
			setValue(0, new0);
		}
		default A getValue0()
		{
			return getValue(0);
		}

		default Unit<A> typedClone() { return TupleImpl.cloneToType(this); }
	}
	public interface Pair<A, B> extends Unit<A> {
		default void setAt1(B new1)
		{
			setValue(1, new1);
		}
		default B getValue1()
		{
			return getValue(1);
		}

		@Override
		default Pair<A, B> typedClone() { return TupleImpl.cloneToType(this); }
	}
	public interface Triplet<A, B, C> extends Pair<A, B> {
		default void setAt2(C new2)
		{
			setValue(2, new2);
		}
		default C getValue2()
		{
			return getValue(2);
		}

		@Override
		default Triplet<A, B, C> typedClone() { return TupleImpl.cloneToType(this); }
	}
	public interface Quartet<A, B, C, D> extends Triplet<A, B, C> {
		default void setAt3(D new3)
		{
			setValue(3, new3);
		}
		default D getValue3()
		{
			return getValue(3);
		}

		@Override
		default Quartet<A, B, C, D> typedClone() { return TupleImpl.cloneToType(this); }
	}
	public interface Quintet<A, B, C, D, E> extends Quartet<A, B, C, D> {
		default void setAt4(E new4)
		{
			setValue(4, new4);
		}
		default E getValue4()
		{
			return getValue(4);
		}

		@Override
		default Quintet<A, B, C, D, E> typedClone() { return TupleImpl.cloneToType(this); }
	}
	public interface Sextet<A, B, C, D, E, F> extends Quintet<A, B, C, D, E> {
		default void setAt5(F new5)
		{
			setValue(5, new5);
		}
		default F getValue5()
		{
			return getValue(5);
		}

		@Override
		default Sextet<A, B, C, D, E, F> typedClone() { return TupleImpl.cloneToType(this); }
	}
	public interface Septet<A, B, C, D, E, F, G> extends Sextet<A, B, C, D, E, F> {
		default void setAt6(G new6)
		{
			setValue(6, new6);
		}
		default G getValue6()
		{
			return getValue(6);
		}

		@Override
		default Septet<A, B, C, D, E, F, G> typedClone() { return TupleImpl.cloneToType(this); }
	}
	public interface Octet<A, B, C, D, E, F, G, H> extends Septet<A, B, C, D, E, F, G> {
		default void setAt7(H new7)
		{
			setValue(7, new7);
		}
		default H getValue7()
		{
			return getValue(7);
		}

		default Octet<A, B, C, D, E, F, G, H> typedClone() { return TupleImpl.cloneToType(this); }
	}
	public interface Ennead<A, B, C, D, E, F, G, H, I> extends Octet<A, B, C, D, E, F, G, H> {
		default void setAt8(I new8)
		{
			setValue(8, new8);
		}
		default I getValue8()
		{
			return getValue(8);
		}

		@Override
		default Ennead<A, B, C, D, E, F, G, H, I> typedClone() { return TupleImpl.cloneToType(this); }
	}
	public interface Decade<A, B, C, D, E, F, G, H, I, J> extends Ennead<A, B, C, D, E, F, G, H, I> {
		default void setAt9(J new9)
		{
			setValue(9, new9);
		}
		default J getValue9()
		{
			return getValue(9);
		}

		@Override
		default Decade<A, B, C, D, E, F, G, H, I, J> typedClone() { return TupleImpl.cloneToType(this); }
	}

	int getSize();
	<S> S getValue(int index);
	<S> void setValue(int index, S newValue);

	Object[] toArray();
	List<Object> toList();

	boolean contains(Object value);
	boolean containsAll(Object... rest);

	int indexOf(Object value);
	int lastIndexOf(Object value);

	Tuple clone();

	public static <A> Unit<A> of(A v0)
	{
		return new TupleImpl.UnitImpl<>(v0);
	}
	public static <A, B> Pair<A, B> of(A v0, B v1)
	{
		return new TupleImpl.PairImpl<>(v0, v1);
	}
	public static <A, B, C> Triplet<A, B, C> of(A v0, B v1, C v2)
	{
		return new TupleImpl.TripletImpl<>(v0, v1, v2);
	}
	public static <A, B, C, D> Quartet<A, B, C, D> of(A v0, B v1, C v2, D v3)
	{
		return new TupleImpl.QuartetImpl<>(v0, v1, v2, v3);
	}
	public static <A, B, C, D, E> Quintet<A, B, C, D, E> of(A v0, B v1, C v2, D v3, E v4)
	{
		return new TupleImpl.QuintetImpl<>(v0, v1, v2, v3, v4);
	}
	public static <A, B, C, D, E, F> Sextet<A, B, C, D, E, F> of(A v0, B v1, C v2, D v3, E v4, F v5)
	{
		return new TupleImpl.SextetImpl<>(v0, v1, v2, v3, v4, v5);
	}
	public static <A, B, C, D, E, F, G> Septet<A, B, C, D, E, F, G> of(A v0, B v1, C v2, D v3, E v4, F v5, G v6)
	{
		return new TupleImpl.SeptetImpl<>(v0, v1, v2, v3, v4, v5, v6);
	}
	public static <A, B, C, D, E, F, G, H> Octet<A, B, C, D, E, F, G, H> of(A v0, B v1, C v2, D v3, E v4, F v5, G v6, H v7)
	{
		return new TupleImpl.OctetImpl<>(v0, v1, v2, v3, v4, v5, v6, v7);
	}
	public static <A, B, C, D, E, F, G, H, I> Ennead<A, B, C, D, E, F, G, H, I> of(A v0, B v1, C v2, D v3, E v4, F v5, G v6, H v7, I v8)
	{
		return new TupleImpl.EnneadImpl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8);
	}
	public static <A, B, C, D, E, F, G, H, I, J> Decade<A, B, C, D, E, F, G, H, I, J> of(A v0, B v1, C v2, D v3, E v4, F v5, G v6, H v7, I v8, J v9)
	{
		return new TupleImpl.DecadeImpl<>(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9);
	}

	public static <A> Unit<A> ofUnit(Object... args)
	{
		return (Unit<A>)of(args[0]);
	}
	public static <A, B> Pair<A, B> ofPair(Object... args)
	{
		return (Pair<A, B>)of(args[0], args[1]);
	}
	public static <A, B, C> Triplet<A, B, C> ofTriplet(Object... args)
	{
		return (Triplet<A, B, C>)of(args[0], args[1], args[2]);
	}
	public static <A, B, C, D> Quartet<A, B, C, D> ofQuartet(Object... args)
	{
		return (Quartet<A, B, C, D>)of(args[0], args[1], args[2], args[3]);
	}
	public static <A, B, C, D, E> Quintet<A, B, C, D, E> ofQuintet(Object... args)
	{
		return (Quintet<A, B, C, D, E>)of(args[0], args[1], args[2], args[3], args[4]);
	}
	public static <A, B, C, D, E, F> Sextet<A, B, C, D, E, F> ofSextet(Object... args)
	{
		return (Sextet<A, B, C, D, E, F>)of(args[0], args[1], args[2], args[3], args[4], args[5]);
	}
	public static <A, B, C, D, E, F, G> Septet<A, B, C, D, E, F, G> ofSeptet(Object... args)
	{
		return (Septet<A, B, C, D, E, F, G>)of(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
	}
	public static <A, B, C, D, E, F, G, H> Octet<A, B, C, D, E, F, G, H> ofOctet(Object... args)
	{
		return (Octet<A, B, C, D, E, F, G, H>)of(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
	}
	public static <A, B, C, D, E, F, G, H, I> Ennead<A, B, C, D, E, F, G, H, I> ofEnnead(Object... args)
	{
		return (Ennead<A, B, C, D, E, F, G, H, I>)of(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]);
	}
	public static <A, B, C, D, E, F, G, H, I, J> Decade<A, B, C, D, E, F, G, H, I, J> ofDecade(Object... args)
	{
		return (Decade<A, B, C, D, E, F, G, H, I, J>)of(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9]);
	}
}
