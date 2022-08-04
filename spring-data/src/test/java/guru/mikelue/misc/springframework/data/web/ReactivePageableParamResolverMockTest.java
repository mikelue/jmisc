package guru.mikelue.misc.springframework.data.web;

import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.SyncHandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;

import org.junit.jupiter.api.Test;

import mockit.Expectations;
import mockit.Mocked;

import static guru.mikelue.misc.springframework.data.web.MockServerWebExchangeUtils.*;
import static guru.mikelue.misc.springframework.data.web.ReactivePageableParamResolver.*;
import static org.assertj.core.api.Assertions.*;

public class ReactivePageableParamResolverMockTest {
	@Mocked
	private BindingContext mockBindCtx;
	@Mocked
	private ApplicationContext appCtx;

	private SyncHandlerMethodArgumentResolver testedResolver =
		new ReactivePageableParamResolver();

	public ReactivePageableParamResolverMockTest() {}

	/**
	 * Tests the resolving value of argument for {@link Pageable} by {@link PageDefault}(and {@link SortDefault}).
	 */
	@Test
	void resolveArgumentValueByPageableDefault()
	{
		var webExchange = EmptyWebExchange;
		setupApplicationContext(webExchange);

		var testedPageable = (Pageable)testedResolver.resolveArgumentValue(
			getMethodParameter(1),
			mockBindCtx, webExchange
		);

		assertPageable(
			testedPageable,
			19, 23
		);

		/**
		 * Assert Sort object by @PageableDefault.sort()
		 */
		assertThat(testedPageable.getSort())
			.extracting("property")
			.containsExactly("k1", "k2");
		// :~)

		testedPageable = (Pageable)testedResolver.resolveArgumentValue(
			getMethodParameter(2),
			mockBindCtx, webExchange
		);

		/**
		 * Assert Sort object by @SortDefault.sort(
		 */
		assertThat(testedPageable.getSort())
			.extracting("property")
			.containsExactly("f1", "f2");
		// :~)
	}

	/**
	 * Tests the resolving value of argument for {@link Pageable} by HTTP headers.
	 */
	@Test
	void resolveArgumentValueByHeader()
	{
		var httpHeaders = new HttpHeaders();
		httpHeaders.add(DEFAULT_PARAM_NAME_PAGE, "4");
		httpHeaders.add(DEFAULT_PARAM_NAME_PAGE_SIZE, "15");

		/**
		 * Unused parameter
		 */
		var httpQueryString = new LinkedMultiValueMap<String, String>();
		httpQueryString.add(DEFAULT_PARAM_NAME_PAGE, "7");
		httpQueryString.add(DEFAULT_PARAM_NAME_PAGE_SIZE, "17");
		// :~)

		var webExchange = build(
			httpHeaders, httpQueryString
		);
		setupApplicationContext(webExchange);

		var testedPageable = (Pageable)testedResolver.resolveArgumentValue(
			getMethodParameter(0),
			mockBindCtx,
			webExchange
		);

		assertPageable(
			testedPageable,
			4, 15
		);
	}

	/**
	 * Tests the resolving value of argument for {@link Pageable} by HTTP query string.
	 */
	@Test
	void resolveArgumentValueByQueryString()
	{
		/**
		 * Unused parameter
		 */
		var httpQueryString = new LinkedMultiValueMap<String, String>();
		httpQueryString.add(DEFAULT_PARAM_NAME_PAGE, "7");
		httpQueryString.add(DEFAULT_PARAM_NAME_PAGE_SIZE, "17");
		// :~)

		var webExchange = build(
			EmptyMultiValueMap, httpQueryString
		);
		setupApplicationContext(webExchange);

		var testedPageable = (Pageable)testedResolver.resolveArgumentValue(
			getMethodParameter(0),
			mockBindCtx,
			webExchange
		);

		assertPageable(
			testedPageable,
			7, 17
		);
	}

	/**
	 * Tests the resolving value of argument for {@link Pageable} by default.
	 */
	@Test
	void resolveArgumentValueByDefault()
	{
		var webExchange = EmptyWebExchange;
		setupApplicationContext(webExchange);

		var testedPageable = (Pageable)testedResolver.resolveArgumentValue(
			getMethodParameter(0),
			mockBindCtx,
			webExchange
		);

		assertPageable(
			testedPageable,
			DEFAULT_PARAM_VALUE_PAGE, DEFAULT_PARAM_VALUE_PAGE_SIZE
		);
	}

	private void assertPageable(Pageable testedPageable, int expectedPage, int expectedPageSize)
	{
		assertThat(testedPageable)
			.hasFieldOrPropertyWithValue("page", expectedPage)
			.hasFieldOrPropertyWithValue("size", expectedPageSize);
	}

	private void setupApplicationContext(ServerWebExchange exchange)
	{
		new Expectations(exchange) {{
			exchange.getApplicationContext();
			result = appCtx;

			appCtx.getBean(ConversionService.class);
			result = new DefaultConversionService();
		}};
	}

	static void sampleMethodParameter(
		Pageable page1,
		@PageableDefault(
			page=19, size=23, sort={ "k1", "k2" }
		)
		Pageable page2,
		@PageableDefault(
			page=1, size=10
		)
		@SortDefault(sort={ "f1", "f2" })
		Pageable page3
	) {}

	private MethodParameter getMethodParameter(int paramIndex)
	{
		return new MethodParameter(
			ReflectionUtils.findMethod(
				getClass(), "sampleMethodParameter",
				Pageable.class, Pageable.class, Pageable.class
			),
			paramIndex
		);
	}
}
