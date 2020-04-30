package guru.mikelue.misc.testlib.validation;

import org.assertj.core.api.Assertions;

import java.util.Set;

import javax.validation.ConstraintViolation;

/**
 * Assertion constructor for {@link ConstraintViolation}.
 */
public class ConstraintViolationAssertions {
	private ConstraintViolationAssertions() {}

	/**
	 * Constructs {@link ConstraintViolationAssert} by single {@link ConstraintViolation}.
	 */
	public static <T> ConstraintViolationAssert<T> assertThat(ConstraintViolation<T> violation)
	{
		return new ConstraintViolationAssert<>(violation);
	}

	/**
	 * Constructs {@link ConstraintViolationAssert} if and only if
	 * the check of single violation is passed.
	 */
	public static <T> ConstraintViolationAssert<T> assertThatAsSingle(Set<ConstraintViolation<T>> violations)
	{
		Assertions.assertThat(violations)
			.hasSize(1);

		return assertThat(
			violations.iterator().next()
		);
	}
}
