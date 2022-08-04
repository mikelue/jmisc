package guru.mikelue.misc.springframework.data.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class MainApplication {
	public MainApplication() {}
}

@Configuration(proxyBeanMethods=false)
class WebConfig implements WebFluxConfigurer {
	WebConfig() {}

	@Override
	public void configureArgumentResolvers(ArgumentResolverConfigurer configurer)
	{
		var sortResolver = new ReactiveSortParamResolver();
		var pageableResolver = new ReactivePageableParamResolver();
		pageableResolver.setSortResolver(sortResolver);

		configurer.addCustomResolver(sortResolver);
		configurer.addCustomResolver(pageableResolver);
	}
}
