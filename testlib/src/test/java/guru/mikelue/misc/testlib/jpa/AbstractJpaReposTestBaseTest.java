package guru.mikelue.misc.testlib.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import org.junit.jupiter.api.Test;

import guru.mikelue.misc.testlib.test.SampleSpringBootConfig;

@DataJpaTest(
	properties={
		"spring.datasource.url=jdbc:h2:mem:sample_1"
	}
)
@ContextConfiguration(classes=SampleSpringBootConfig.class)
public class AbstractJpaReposTestBaseTest extends AbstractJpaReposTestBase {
	public AbstractJpaReposTestBaseTest() {}

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
