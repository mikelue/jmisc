package guru.mikelue.misc.testlib.logger;

import java.util.function.Consumer;

import org.springframework.test.web.reactive.server.ExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This logger can be used as {@link WebTestClient} to provide
 * convenient way to output the content of {@link ExchangeResult} for
 * testing.
 *
 * <h2>toXXX methods</h2>
 * <p>
 * Output the content of {@link ExchangeResult} to corresponding level of logging.
 * </p>
 *
 * <h2>asXXX methods</h2>
 * <p>
 * Builds a {@link Consumer} with customized message fed to message format of SLF4j.
 * </p>
 *
 * @see <a href="http://www.slf4j.org/apidocs/org/slf4j/Logger.html">SLF4j Logger</a>
 */
public class ExchangeResultLogger {
	private final Logger logger;

	public ExchangeResultLogger(String name)
	{
		this(LoggerFactory.getLogger(name));
	}
	public ExchangeResultLogger(Class<?> clazz)
	{
		this(LoggerFactory.getLogger(clazz));
	}
	public ExchangeResultLogger(Logger logger)
	{
		this.logger = logger;
	}

	public void toError(ExchangeResult result)
	{
		logger.error("{}", result);
	}
	public void toWarn(ExchangeResult result)
	{
		logger.warn("{}", result);
	}
	public void toInfo(ExchangeResult result)
	{
		logger.info("{}", result);
	}
	public void toDebug(ExchangeResult result)
	{
		logger.debug("{}", result);
	}
	public void toTrace(ExchangeResult result)
	{
		logger.trace("{}", result);
	}

	public <T> Consumer<? extends ExchangeResult> asError(String message)
	{
		return e -> logger.error(message, e);
	}
	public <T> Consumer<? extends ExchangeResult> asWarn(String message)
	{
		return e -> logger.warn(message, e);
	}
	public <T> Consumer<? extends ExchangeResult> asInfo(String message)
	{
		return e -> logger.info(message, e);
	}
	public <T> Consumer<? extends ExchangeResult> asDebug(String message)
	{
		return e -> logger.debug(message, e);
	}
	public <T> Consumer<? extends ExchangeResult> asTrace(String message)
	{
		return e -> logger.trace(message, e);
	}
}
