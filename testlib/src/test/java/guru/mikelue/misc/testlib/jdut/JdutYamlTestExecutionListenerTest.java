package guru.mikelue.misc.testlib.jdut;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.context.TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;

import guru.mikelue.jdut.annotation.JdutResource;
import guru.mikelue.misc.testlib.AbstractTestBase;
import guru.mikelue.misc.testlib.test.SampleSpringBootConfig;

@JdbcTest
@TestPropertySource(
	properties={
		"spring.datasource.url=jdbc:h2:mem:sample_71"
	}
)
@TestExecutionListeners(
	listeners=JdutYamlTestExecutionListener.class,
	mergeMode=MERGE_WITH_DEFAULTS
)
@ContextConfiguration(classes=SampleSpringBootConfig.class)
public class JdutYamlTestExecutionListenerTest extends AbstractTestBase {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public JdutYamlTestExecutionListenerTest() {}

	@Test @JdutResource
	void loadData()
	{
		var testedResult = jdbcTemplate.queryForList("SELECT * FROM car");
		assertThat(testedResult).hasSize(2);
	}

	/**
	 * Tests that the JDUT is conducting data before transaction manager of SpringFramework.
	 */
	@Test @JdutResource
	@Transactional
	void updateData()
	{
		jdbcTemplate.execute(
			"UPDATE grille SET name = 'KKC-99' WHERE id = 1"
		);
	}
}
