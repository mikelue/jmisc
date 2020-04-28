package guru.mikelue.misc.testlib.test;

import javax.sql.DataSource;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;

import guru.mikelue.jdut.yaml.YamlConductorFactory;

@EnableAutoConfiguration
@SpringBootConfiguration
public class SampleSpringBootConfig {
	public SampleSpringBootConfig() {}

	@Bean
	YamlConductorFactory yamlConductorFactory(DataSource dataSource)
	{
		return YamlConductorFactory.build(dataSource);
	}
}
