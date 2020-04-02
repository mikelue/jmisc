package guru.mikelue.misc.testlib.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import org.junit.jupiter.api.Test;

import guru.mikelue.misc.testlib.test.SampleSpringBootConfig;

@ContextConfiguration(classes=SampleSpringBootConfig.class)
@TestPropertySource(
	properties={
		"spring.datasource.url=jdbc:h2:mem:sample_2"
	}
)
public class SpringBootJpaReposTestBaseTest extends SpringBootJpaReposTestBase {
	public SpringBootJpaReposTestBaseTest() {}

	/**
	 * Tests the existence for instant of bean.
	 */
	@Test
	void doGetEntityManager()
	{
		assertThat(getEntityManager()).isNotNull();
	}

	/**
	 * Tests the existence for instant of bean.
	 */
	@Test
	void doGetDataSource()
	{
		assertThat(getDataSource()).isNotNull();
	}
}
