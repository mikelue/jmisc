package guru.mikelue.misc.testlib.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Base environment for testing web with injected {@link WebTestClient}.
 *
 * This base class is often used with {@link WebFluxTest} or {@link SpringBootTest}.
 */
public abstract class SpringWebClientTestBase extends AbstractSpringWebTestBase {
	@Autowired
	private WebTestClient webTestClient;

	protected SpringWebClientTestBase() {}

	public WebTestClient getWebTestClient()
	{
		return webTestClient;
	}
}
