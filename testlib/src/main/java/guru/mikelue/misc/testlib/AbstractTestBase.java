package guru.mikelue.misc.testlib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTestBase {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected AbstractTestBase() {}

	public Logger getLogger()
	{
		return logger;
	}
}
