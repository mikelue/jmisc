package guru.mikelue.misc.testlib.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;

@SpringBootTest(
	classes=ValidationAutoConfiguration.class,
	properties={
		"spring.main.banner-mode=off"
	}
)
public class ConstraintViolationAssertTest extends AbstractSpringValidatorTestBase {
	public ConstraintViolationAssertTest() {}

	/**
	 * Tests the assertion for type of annotation of constraint.
	 */
	@Test
	void constraintIsTypeOfAnnotation()
	{
		ConstraintViolationAssertions.assertThat(
			validateValue(BeechMushroom.class, "value", 7)
				.iterator().next()
		)
			.constraintIsTypeOfAnnotation(Min.class);
	}

	/**
	 * Tests the validation with failed properties.
	 */
	@Test
	void assertThatAsSingle()
	{
		var violation = ConstraintViolationAssertions.assertThatAsSingle(
			validateValue(BeechMushroom.class, "model", "ZC-001")
		)
			.getViolation();

		assertThat(violation.getPropertyPath())
			.extracting("name", String.class)
			.containsExactly("model");
	}

	@Test
	void extractNameOfPropertyPath()
	{
		var tree = new AlmondTree();
		tree.m1 = newValidMushroom();
		tree.m2 = newValidMushroom();

		tree.m2.value = 5;

		ConstraintViolationAssertions.assertThatAsSingle(
			validate(tree)
		)
			.extractNameOfPropertyPath()
			.containsExactly("m2", "value");
	}

	private static BeechMushroom newValidMushroom()
	{
		return new BeechMushroom("ZK-102", 11);
	}
}

class AlmondTree {
	@Valid
	BeechMushroom m1;
	@Valid
	BeechMushroom m2;
}

class BeechMushroom {
	@Pattern(regexp="ZK-\\d{3}")
	String model;
	@Min(10)
	int value;

	BeechMushroom(
		String newModel, int newValue
	) {
		model = newModel;
		value = newValue;
	}
}
