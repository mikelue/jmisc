package guru.mikelue.misc.testlib.jpa;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public abstract class SpringBootJpaReposTestBase extends AbstractJpaReposTestBase {
	protected SpringBootJpaReposTestBase() {}
}
