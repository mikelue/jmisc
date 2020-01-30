package guru.mikelue.misc.lang.data;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import guru.mikelue.misc.lang.function.IntFunctionUtils;

/**
 * Reusable builder keeps {@link Supplier} to generate data.
 *
 * Any instance of builder is not thread-safe.
 *
 * You can use {@link IntFunctionUtils} to convert {@link Supplier} to {@link IntFunction}.
 *
 * @param <T> Type of generated data
 *
 * @see BagDataFactory
 * @see #of
 */
public class BagDataBuilder<T> implements BagDataBuilderBase<T> {
	private final IntFunction<T> supplier;
	private final T[] EMPTY_OBJECT_ARRAY;

	public static class IntForge extends BagDataBuilder<Integer> {
		private final IntUnaryOperator supplierImpl;

		public static BagDataBuilder<Integer> of(IntUnaryOperator supplier)
		{
			return new IntForge(supplier);
		}

		private IntForge(IntUnaryOperator supplier)
		{
			super(Integer.class, supplier::applyAsInt);
			this.supplierImpl = supplier;
		}

		public int[] buildIntArray(int size)
		{
			return BagDataFactory.Arrays.buildInts(size, getSupplier());
		}

		public IntStream buildIntStream(int size)
		{
			return IntStream.range(0, size)
				.map(supplierImpl);
		}
	}
	public static class LongForge extends BagDataBuilder<Long> {
		private final IntToLongFunction supplierImpl;

		public static BagDataBuilder<Long> of(IntToLongFunction supplier)
		{
			return new LongForge(supplier);
		}

		private LongForge(IntToLongFunction supplier)
		{
			super(Long.class, supplier::applyAsLong);
			this.supplierImpl = supplier;
		}

		public long[] buildLongArray(int size)
		{
			return BagDataFactory.Arrays.buildLongs(size, getSupplier());
		}

		public LongStream buildLongStream(int size)
		{
			return IntStream.range(0, size)
				.mapToLong(supplierImpl);
		}
	}
	public static class DoubleForge extends BagDataBuilder<Double> {
		private final IntToDoubleFunction supplierImpl;

		public static BagDataBuilder<Double> of(IntToDoubleFunction supplier)
		{
			return new DoubleForge(supplier);
		}

		private DoubleForge(IntToDoubleFunction supplier)
		{
			super(Double.class, supplier::applyAsDouble);
			this.supplierImpl = supplier;
		}

		public double[] buildDoubleArray(int size)
		{
			return BagDataFactory.Arrays.buildDoubles(size, getSupplier());
		}

		public DoubleStream buildDoubleStream(int size)
		{
			return IntStream.range(0, size)
				.mapToDouble(supplierImpl);
		}
	}

	public static <S> BagDataBuilder<S> of(Class<S> typeOfElement, IntFunction<? extends S> supplier)
	{
		return new BagDataBuilder<>(typeOfElement, supplier);
	}

	@SuppressWarnings("unchecked")
	protected BagDataBuilder(Class<T> typeOfElement, IntFunction<? extends T> supplier)
	{
		this.supplier = (IntFunction<T>)supplier;
		EMPTY_OBJECT_ARRAY = (T[])Array.newInstance(typeOfElement, 0);
	}

	protected IntFunction<T> getSupplier()
	{
		return supplier;
	}

	@Override
	public T[] buildArray(int size)
	{
		return BagDataFactory.buildArray(size, supplier, EMPTY_OBJECT_ARRAY);
	}

	@Override
	public List<T> buildList(int size)
	{
		return BagDataFactory.buildList(size, supplier);
	}

	@Override
	public Set<T> buildSet(int size)
	{
		return BagDataFactory.buildSet(size, supplier);
	}

	@Override
	public Stream<T> buildStream(int size)
	{
		return BagDataFactory.buildStream(size, supplier);
	}
}
