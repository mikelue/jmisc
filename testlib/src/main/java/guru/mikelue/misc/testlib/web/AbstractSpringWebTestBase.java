package guru.mikelue.misc.testlib.web;

import org.springframework.test.web.reactive.server.WebTestClient;

import guru.mikelue.misc.testlib.AbstractTestBase;
import guru.mikelue.misc.testlib.logger.ExchangeResultLogger;

/**
 * Base environment for testing web by {@link WebTestClient}.
 *
 * @see #getLogger
 * @see #getExchangeResultLogger
 * @see ExchangeResultLogger
 */
public abstract class AbstractSpringWebTestBase extends AbstractTestBase {
	private final ExchangeResultLogger exchangeResultlogger = new ExchangeResultLogger(getLogger());

	protected AbstractSpringWebTestBase() {}

	public ExchangeResultLogger getExchangeResultLogger()
	{
		return exchangeResultlogger;
	}
}
