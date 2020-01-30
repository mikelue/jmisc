package guru.mikelue.misc.lang.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import guru.mikelue.misc.lang.tuple.Tuple;
import guru.mikelue.misc.lang.tuple.Tuple.*;

/**
 * Performs <a href="https://en.wikipedia.org/wiki/Cartesian_product">Cartesian product</a> and
 * gives corresponding instance of {@link Tuple} type.
 */
public interface CartesianProduct {
	static <A, B> Stream<Pair<A, B>> fold(Stream<A> s0, Stream<B> s1)
	{
		return Impl.fold(
			() -> Tuple.ofPair(new Object[2]),
			s0, s1
		);
	}
	static <A, B, C> Stream<Triplet<A, B, C>> fold(Stream<A> s0, Stream<B> s1, Stream<C> s2)
	{
		return Impl.fold(
			() -> Tuple.ofTriplet(new Object[3]),
			s0, s1, s2
		);
	}
	static <A, B, C, D> Stream<Quartet<A, B, C, D>> fold(Stream<A> s0, Stream<B> s1, Stream<C> s2, Stream<D> s3)
	{
		return Impl.fold(
			() -> Tuple.ofQuartet(new Object[4]),
			s0, s1, s2, s3
		);
	}
	static <A, B, C, D, E> Stream<Quintet<A, B, C, D, E>> fold(Stream<A> s0, Stream<B> s1, Stream<C> s2, Stream<D> s3, Stream<E> s4)
	{
		return Impl.fold(
			() -> Tuple.ofQuintet(new Object[5]),
			s0, s1, s2, s3, s4
		);
	}
	static <A, B, C, D, E, F> Stream<Sextet<A, B, C, D, E, F>> fold(Stream<A> s0, Stream<B> s1, Stream<C> s2, Stream<D> s3, Stream<E> s4, Stream<F> s5)
	{
		return Impl.fold(
			() -> Tuple.ofSextet(new Object[6]),
			s0, s1, s2, s3, s4, s5
		);
	}
	static <A, B, C, D, E, F, G> Stream<Septet<A, B, C, D, E, F, G>> fold(Stream<A> s0, Stream<B> s1, Stream<C> s2, Stream<D> s3, Stream<E> s4, Stream<F> s5, Stream<G> s6)
	{
		return Impl.fold(
			() -> Tuple.ofSeptet(new Object[7]),
			s0, s1, s2, s3, s4, s5, s6
		);
	}
	static <A, B, C, D, E, F, G, H> Stream<Octet<A, B, C, D, E, F, G, H>> fold(Stream<A> s0, Stream<B> s1, Stream<C> s2, Stream<D> s3, Stream<E> s4, Stream<F> s5, Stream<G> s6, Stream<H> s7)
	{
		return Impl.fold(
			() -> Tuple.ofOctet(new Object[8]),
			s0, s1, s2, s3, s4, s5, s6, s7
		);
	}
	static <A, B, C, D, E, F, G, H, I> Stream<Ennead<A, B, C, D, E, F, G, H, I>> fold(Stream<A> s0, Stream<B> s1, Stream<C> s2, Stream<D> s3, Stream<E> s4, Stream<F> s5, Stream<G> s6, Stream<H> s7, Stream<I> s8)
	{
		return Impl.fold(
			() -> Tuple.ofEnnead(new Object[9]),
			s0, s1, s2, s3, s4, s5, s6, s7, s8
		);
	}
	static <A, B, C, D, E, F, G, H, I, J> Stream<Decade<A, B, C, D, E, F, G, H, I, J>> fold(Stream<A> s0, Stream<B> s1, Stream<C> s2, Stream<D> s3, Stream<E> s4, Stream<F> s5, Stream<G> s6, Stream<H> s7, Stream<I> s8, Stream<J> s9)
	{
		return Impl.fold(
			() -> Tuple.ofDecade(new Object[10]),
			s0, s1, s2, s3, s4, s5, s6, s7, s8, s9
		);
	}

	static <A, B> Iterable<Pair<A, B>> fold(Iterable<A> s0, Iterable<B> s1)
	{
		return Impl.fold(
			() -> Tuple.ofPair(new Object[2]),
			s0, s1
		);
	}
	static <A, B, C> Iterable<Triplet<A, B, C>> fold(Iterable<A> s0, Iterable<B> s1, Iterable<C> s2)
	{
		return Impl.fold(
			() -> Tuple.ofTriplet(new Object[3]),
			s0, s1, s2
		);
	}
	static <A, B, C, D> Iterable<Quartet<A, B, C, D>> fold(Iterable<A> s0, Iterable<B> s1, Iterable<C> s2, Iterable<D> s3)
	{
		return Impl.fold(
			() -> Tuple.ofQuartet(new Object[4]),
			s0, s1, s2, s3
		);
	}
	static <A, B, C, D, E> Iterable<Quintet<A, B, C, D, E>> fold(Iterable<A> s0, Iterable<B> s1, Iterable<C> s2, Iterable<D> s3, Iterable<E> s4)
	{
		return Impl.fold(
			() -> Tuple.ofQuintet(new Object[5]),
			s0, s1, s2, s3, s4
		);
	}
	static <A, B, C, D, E, F> Iterable<Sextet<A, B, C, D, E, F>> fold(Iterable<A> s0, Iterable<B> s1, Iterable<C> s2, Iterable<D> s3, Iterable<E> s4, Iterable<F> s5)
	{
		return Impl.fold(
			() -> Tuple.ofSextet(new Object[6]),
			s0, s1, s2, s3, s4, s5
		);
	}
	static <A, B, C, D, E, F, G> Iterable<Septet<A, B, C, D, E, F, G>> fold(Iterable<A> s0, Iterable<B> s1, Iterable<C> s2, Iterable<D> s3, Iterable<E> s4, Iterable<F> s5, Iterable<G> s6)
	{
		return Impl.fold(
			() -> Tuple.ofSeptet(new Object[7]),
			s0, s1, s2, s3, s4, s5, s6
		);
	}
	static <A, B, C, D, E, F, G, H> Iterable<Octet<A, B, C, D, E, F, G, H>> fold(Iterable<A> s0, Iterable<B> s1, Iterable<C> s2, Iterable<D> s3, Iterable<E> s4, Iterable<F> s5, Iterable<G> s6, Iterable<H> s7)
	{
		return Impl.fold(
			() -> Tuple.ofOctet(new Object[8]),
			s0, s1, s2, s3, s4, s5, s6, s7
		);
	}
	static <A, B, C, D, E, F, G, H, I> Iterable<Ennead<A, B, C, D, E, F, G, H, I>> fold(Iterable<A> s0, Iterable<B> s1, Iterable<C> s2, Iterable<D> s3, Iterable<E> s4, Iterable<F> s5, Iterable<G> s6, Iterable<H> s7, Iterable<I> s8)
	{
		return Impl.fold(
			() -> Tuple.ofEnnead(new Object[9]),
			s0, s1, s2, s3, s4, s5, s6, s7, s8
		);
	}
	static <A, B, C, D, E, F, G, H, I, J> Iterable<Decade<A, B, C, D, E, F, G, H, I, J>> fold(Iterable<A> s0, Iterable<B> s1, Iterable<C> s2, Iterable<D> s3, Iterable<E> s4, Iterable<F> s5, Iterable<G> s6, Iterable<H> s7, Iterable<I> s8, Iterable<J> s9)
	{
		return Impl.fold(
			() -> Tuple.ofDecade(new Object[10]),
			s0, s1, s2, s3, s4, s5, s6, s7, s8, s9
		);
	}

	interface Impl {
		static <T extends Tuple> List<T> fold(Supplier<Tuple> dataSupplier, Iterable<?>... dataSets)
		{
			return fold(dataSupplier, t -> {}, new LinkedList<Tuple>(), 0, dataSets);
		}
		@SuppressWarnings("unchecked")
		private static <T extends Tuple> List<T> fold(Supplier<Tuple> dataSupplier, Consumer<Tuple> tupleSetter, List<Tuple> result, int index, Iterable<?>... dataSets)
		{
			if (dataSets.length == 1) {
				for (var value: dataSets[0]) {
					var newTuple = dataSupplier.get();

	 				tupleSetter
						.andThen(t -> t.setValue(index, value))
						.accept(newTuple);

					result.add(newTuple);
				}

				return (List<T>)result;
			}

			for (var value: dataSets[0]) {
				fold(
					dataSupplier,
					tupleSetter.andThen(
						t -> t.setValue(index, value)
					),
					result,
					index + 1,
					Arrays.copyOfRange(dataSets, 1, dataSets.length)
				);
			}

			return (List<T>)result;
		}
		@SuppressWarnings("unchecked")
		static <T extends Tuple> Stream<T> fold(Supplier<Tuple> dataSupplier, Stream<?>... dataSets)
		{
			var asList = new ArrayList<Iterable<?>>(dataSets.length);

			for (var dataStream: dataSets) {
				asList.add(dataStream.collect(Collectors.toList()));
			}

			return (Stream<T>)fold(dataSupplier, asList.toArray(new Iterable<?>[0]))
				.stream();
		}
	}
}
