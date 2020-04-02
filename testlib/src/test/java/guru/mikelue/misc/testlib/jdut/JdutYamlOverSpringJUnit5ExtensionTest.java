package guru.mikelue.misc.testlib.jdut;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import guru.mikelue.jdut.annotation.JdutResource;
import guru.mikelue.misc.testlib.AbstractTestBase;
import guru.mikelue.misc.testlib.test.SampleSpringBootConfig;

@JdbcTest
@ExtendWith(JdutYamlOverSpringJUnit5Extension.class)
@TestPropertySource(
	properties={
		"spring.datasource.url=jdbc:h2:mem:sample_71"
	}
)
@ContextConfiguration(classes=SampleSpringBootConfig.class)
public class JdutYamlOverSpringJUnit5ExtensionTest extends AbstractTestBase {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public JdutYamlOverSpringJUnit5ExtensionTest() {}

	@Test @JdutResource
	@Sql(executionPhase=BEFORE_TEST_METHOD,
		statements={
			"CREATE TABLE car(id int primary key, name varchar(32))"
		}
	)
	@Sql(executionPhase=AFTER_TEST_METHOD,
		statements={
			"DROP TABLE car"
		}
	)
	void loadData()
	{
		var testedResult = jdbcTemplate.queryForList("SELECT * FROM car");
		assertThat(testedResult).hasSize(2);
	}
}
