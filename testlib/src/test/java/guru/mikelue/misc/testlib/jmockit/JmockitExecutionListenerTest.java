package guru.mikelue.misc.testlib.jmockit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import org.junit.jupiter.api.Test;

import guru.mikelue.misc.testlib.AbstractTestBase;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

@SpringJUnitConfig(classes={JmockitListenerConfig.class})
@TestExecutionListeners(
	listeners=JmockitExecutionListener.class,
	mergeMode=MERGE_WITH_DEFAULTS
)
public class JmockitExecutionListenerTest extends AbstractTestBase {
	@Tested(fullyInitialized=true)
	private SampleService testedService;

	@Injectable
	private SampleDependency mockDependency;

	public JmockitExecutionListenerTest() {}

	@Test
	void injectedDependency()
	{
		new Expectations() {{
			mockDependency.getValue();
			result = 31;
		}};

		assertThat(testedService.getValue())
			.isEqualTo(31);
	}
}

@Configuration
class JmockitListenerConfig {
	@Bean
	SampleService sampleService()
	{
		return new SampleService();
	}

	@Bean
	SampleDependency sampleDependency()
	{
		return new SampleDependency();
	}
}

class SampleService {
	SampleService() {}

	@Autowired
	private SampleDependency dep;

	public int getValue()
	{
		return dep.getValue();
	}
}

class SampleDependency {
	SampleDependency() {}

	public int getValue()
	{
		return 20;
	}
}
