package guru.mikelue.misc.lang.function;

import java.util.function.DoubleSupplier;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

public interface IntFunctionUtils {
	public static <T> IntFunction<T> from(Supplier<? extends T> supplier)
	{
		return i -> supplier.get();
	}
	public static IntUnaryOperator toUnary(IntSupplier supplier)
	{
		return i -> supplier.getAsInt();
	}
	public static IntToLongFunction toLong(LongSupplier supplier)
	{
		return i -> supplier.getAsLong();
	}
	public static IntToDoubleFunction toDouble(DoubleSupplier supplier)
	{
		return i -> supplier.getAsDouble();
	}
}
