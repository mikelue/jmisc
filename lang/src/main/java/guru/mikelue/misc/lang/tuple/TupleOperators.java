package guru.mikelue.misc.lang.tuple;

import guru.mikelue.misc.lang.tuple.Tuple.*;

public interface TupleOperators {
	public static <A, B> Pair<A, B> addAsPair(Tuple left, Tuple right)
	{
		var implObject = (TupleImpl)left;
		return implObject.addWithLimitSize(2, right);
	}
	public static <A, B, C> Triplet<A, B, C> addAsTriplet(Tuple left, Tuple right)
	{
		var implObject = (TupleImpl)left;
		return implObject.addWithLimitSize(3, right);
	}
	public static <A, B, C, D> Quartet<A, B, C, D> addAsQuartet(Tuple left, Tuple right)
	{
		var implObject = (TupleImpl)left;
		return implObject.addWithLimitSize(4, right);
	}
	public static <A, B, C, D, E> Quintet<A, B, C, D, E> addAsQuintet(Tuple left, Tuple right)
	{
		var implObject = (TupleImpl)left;
		return implObject.addWithLimitSize(5, right);
	}
	public static <A, B, C, D, E, F> Sextet<A, B, C, D, E, F> addAsSextet(Tuple left, Tuple right)
	{
		var implObject = (TupleImpl)left;
		return implObject.addWithLimitSize(6, right);
	}
	public static <A, B, C, D, E, F, G> Septet<A, B, C, D, E, F, G> addAsSeptet(Tuple left, Tuple right)
	{
		var implObject = (TupleImpl)left;
		return implObject.addWithLimitSize(7, right);
	}
	public static <A, B, C, D, E, F, G, H> Octet<A, B, C, D, E, F, G, H> addAsOctet(Tuple left, Tuple right)
	{
		var implObject = (TupleImpl)left;
		return implObject.addWithLimitSize(8, right);
	}
	public static <A, B, C, D, E, F, G, H, I> Ennead<A, B, C, D, E, F, G, H, I> addAsEnnead(Tuple left, Tuple right)
	{
		var implObject = (TupleImpl)left;
		return implObject.addWithLimitSize(9, right);
	}
	public static <A, B, C, D, E, F, G, H, I, J> Decade<A, B, C, D, E, F, G, H, I, J> addAsDecade(Tuple left, Tuple right)
	{
		var implObject = (TupleImpl)left;
		return implObject.addWithLimitSize(10, right);
	}

	public static <A, B> Pair<A, B> addAtAsPair(Tuple left, Tuple right, int atPos)
	{
		var implObject = (TupleImpl)left;
		return implObject.addWithLimitSize(2, right, atPos);
	}
	public static <A, B, C> Triplet<A, B, C> addAtAsTriplet(Tuple left, Tuple right, int atPos)
	{
		var implObject = (TupleImpl)left;
		return implObject.addWithLimitSize(3, right, atPos);
	}
	public static <A, B, C, D> Quartet<A, B, C, D> addAtAsQuartet(Tuple left, Tuple right, int atPos)
	{
		var implObject = (TupleImpl)left;
		return implObject.addWithLimitSize(4, right, atPos);
	}
	public static <A, B, C, D, E> Quintet<A, B, C, D, E> addAtAsQuintet(Tuple left, Tuple right, int atPos)
	{
		var implObject = (TupleImpl)left;
		return implObject.addWithLimitSize(5, right, atPos);
	}
	public static <A, B, C, D, E, F> Sextet<A, B, C, D, E, F> addAtAsSextet(Tuple left, Tuple right, int atPos)
	{
		var implObject = (TupleImpl)left;
		return implObject.addWithLimitSize(6, right, atPos);
	}
	public static <A, B, C, D, E, F, G> Septet<A, B, C, D, E, F, G> addAtAsSeptet(Tuple left, Tuple right, int atPos)
	{
		var implObject = (TupleImpl)left;
		return implObject.addWithLimitSize(7, right, atPos);
	}
	public static <A, B, C, D, E, F, G, H> Octet<A, B, C, D, E, F, G, H> addAtAsOctet(Tuple left, Tuple right, int atPos)
	{
		var implObject = (TupleImpl)left;
		return implObject.addWithLimitSize(8, right, atPos);
	}
	public static <A, B, C, D, E, F, G, H, I> Ennead<A, B, C, D, E, F, G, H, I> addAtAsEnnead(Tuple left, Tuple right, int atPos)
	{
		var implObject = (TupleImpl)left;
		return implObject.addWithLimitSize(9, right, atPos);
	}
	public static <A, B, C, D, E, F, G, H, I, J> Decade<A, B, C, D, E, F, G, H, I, J> addAtAsDecade(Tuple left, Tuple right, int atPos)
	{
		var implObject = (TupleImpl)left;
		return implObject.addWithLimitSize(10, right, atPos);
	}
}
