package guru.mikelue.misc.testlib.validation;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@SpringBootTest(
	classes=ValidationAutoConfiguration.class,
	properties={
		"spring.main.banner-mode=off"
	}
)
public class AbstractSpringValidatorTestBaseTest extends AbstractSpringValidatorTestBase {
	public AbstractSpringValidatorTestBaseTest() {}

	/**
	 * Tests the injection of validator object
	 */
	@Test
	void validator()
	{
		Assertions.assertThat(getValidator()).isNotNull();
	}

	@Test
	void validate()
	{
		Assertions.assertThat(super.validate(newValidMushroom()))
			.hasSize(0);
	}

	@Test
	void validateProperty()
	{
		var mushroom = newValidMushroom();

		Assertions.assertThat(
			super.validateProperty(mushroom, "value")
		)
			.hasSize(0);
	}

	@Test
	void validateValue()
	{
		Assertions.assertThat(
			super.validateValue(OysterMushroom.class, "value", 10)
		)
			.hasSize(0);
	}

	private static OysterMushroom newValidMushroom()
	{
		return new OysterMushroom("ZK-102", 11);
	}
}

class OysterMushroom {
	@Pattern(regexp="ZK-\\d{3}")
	String model;
	@Min(10)
	int value;

	OysterMushroom(
		String newModel, int newValue
	) {
		model = newModel;
		value = newValue;
	}
}
