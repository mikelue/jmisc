package guru.mikelue.misc.testlib.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.test.context.ContextConfiguration;

import org.junit.jupiter.api.Test;

import guru.mikelue.misc.testlib.test.SampleSpringBootConfig;

@ContextConfiguration(classes=SampleSpringBootConfig.class)
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
