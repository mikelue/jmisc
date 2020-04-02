package guru.mikelue.misc.testlib.jdut;

import javax.sql.DataSource;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import guru.mikelue.jdut.junit5.JdutYamlFactory;
import guru.mikelue.jdut.yaml.YamlConductorFactory;

/**
 * This extension loads {@link DataSource} from bean of SpringFramework.
 */
public class JdutYamlOverSpringJUnit5Extension extends JdutYamlFactory {
	private final static String CONDUCTOR_KEY_NAME = "_jdut_conductor_";

	@Override
	public void beforeAll(ExtensionContext context) throws Exception
	{
		/**
		 * Puts JDUT conductor to namespace of test class
		 */
		getStore(context).put(
			CONDUCTOR_KEY_NAME,
			buildConductorFactory(context)
		);
		// :~)

		super.beforeAll(context);
	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception
	{
		super.afterAll(context);

		/**
		 * Removes JDUT conductor from namespace of test class
		 */
		getStore(context).remove(CONDUCTOR_KEY_NAME);
		// :~)
	}

	@Override
	protected YamlConductorFactory getYamlConductorFactory(ExtensionContext context, Event event)
	{
		var store = context.getStore(
			Namespace.create(context.getTestClass())
		);

		return (YamlConductorFactory)store.get(CONDUCTOR_KEY_NAME);
	}

	private YamlConductorFactory buildConductorFactory(ExtensionContext context)
	{
		var dataSource = SpringExtension.getApplicationContext(context)
			.getBean(DataSource.class);

		return YamlConductorFactory.build(dataSource);
	}
	private Store getStore(ExtensionContext context)
	{
		return context.getStore(
			Namespace.create(context.getTestClass())
		);
	}
}
