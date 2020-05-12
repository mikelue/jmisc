package guru.mikelue.misc.testlib.jmockit;

import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import mockit.integration.springframework.FakeBeanFactory;

/**
 * This listener calls new {@link FakeBeanFactory} to initialize JMockit support of SpringFramework.
 *
 * The order(1980) is intended to be higher priority than {@link DependencyInjectionTestExecutionListener}(2000).
 *
 * Usage example
 * <pre>{@code
 * @TestExecutionListeners(
 *    listeners=JMockitExecutionListener.class,
 *    mergeMode=MERGE_WITH_DEFAULTS
 * )
 * }</pre>
 *
 * @see <a href="http://jmockit.github.io/tutorial/EnterpriseApplications.html#spring">Integration JMockit with SpringFramework</a>
 */
public class JmockitExecutionListener implements TestExecutionListener, Ordered {
	public JmockitExecutionListener() {}

	@Override
	public void beforeTestClass(TestContext testContext)
	{
		new FakeBeanFactory();
	}

	@Override
	public int getOrder()
	{
		return 1980;
	}
}
