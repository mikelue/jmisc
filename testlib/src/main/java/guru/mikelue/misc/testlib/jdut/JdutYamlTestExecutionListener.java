package guru.mikelue.misc.testlib.jdut;

import org.springframework.core.annotation.Order;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import guru.mikelue.jdut.DuetConductor;
import guru.mikelue.jdut.annotation.AnnotationUtil;
import guru.mikelue.jdut.yaml.YamlConductorFactory;

/**
 * This listener is intent to be used with {@link TestExecutionListeners} on SpringFramework.
 *
 * The order of this listener is set before {@link TransactionalTestExecutionListener}(4000).
 *
 * Usage:
 * <pre>{@code
 * @TestExecutionListeners(
 *     listeners=JdutYamlTestExecutionListener.class,
 *     mergeMode=MERGE_WITH_DEFAULTS
 * )
 * }</pre>
 */
@Order(3980)
public class JdutYamlTestExecutionListener implements TestExecutionListener {
	public JdutYamlTestExecutionListener() {}

	@Override
	public void beforeTestClass(TestContext testContext)
	{
		var yamlConductorFactory = testContext.getApplicationContext()
			.getBean(YamlConductorFactory.class);

		var testClass = testContext.getTestClass();
		var duetConductor = AnnotationUtil.buildConductorByConvention(
			yamlConductorFactory, testClass
		);

		duetConductor.ifPresent(
			conductor -> {
				conductor.build();
				testContext.setAttribute(testClass.getCanonicalName(), conductor);
			}
		);
	}
	@Override
	public void afterTestClass(TestContext testContext)
	{
		var testClassKey = testContext.getTestClass().getCanonicalName();
		if (!testContext.hasAttribute(testClassKey)) {
			return;
		}

		var duetConductor = (DuetConductor)testContext.removeAttribute(testClassKey);
		duetConductor.clean();
	}
	@Override
	public void beforeTestMethod(TestContext testContext)
	{
		var yamlConductorFactory = testContext.getApplicationContext()
			.getBean(YamlConductorFactory.class);

		var testMethod = testContext.getTestMethod();
		var duetConductor = AnnotationUtil.buildConductorByConvention(
			yamlConductorFactory, testMethod
		);

		duetConductor.ifPresent(
			conductor -> {
				conductor.build();
				testContext.setAttribute(testMethod.toString(), conductor);
			}
		);
	}
	@Override
	public void afterTestMethod(TestContext testContext)
	{
		var testMethodKey = testContext.getTestMethod().toString();
		if (!testContext.hasAttribute(testMethodKey)) {
			return;
		}

		var duetConductor = (DuetConductor)testContext.removeAttribute(testMethodKey);
		duetConductor.clean();
	}
}
