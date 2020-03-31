package guru.mikelue.misc.hibernate.env;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

public class Slf4jConfigurerTest {
	static {
		Slf4jConfigurer.jbossLoggingToSlf4j();
	}

	public Slf4jConfigurerTest() {}

	/**
	 * Tests the setting of system property.
	 */
	@Test
	void jbossLoggingToSlf4j()
	{
		assertThat(System.getProperty(Slf4jConfigurer.JBOSS_LOGGING_PROVIDER))
			.isEqualTo("slf4j");
	}
}
