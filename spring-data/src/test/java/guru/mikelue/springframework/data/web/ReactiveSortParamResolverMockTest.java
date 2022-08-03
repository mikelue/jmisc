package guru.mikelue.springframework.data.web;

import static org.assertj.core.api.Assertions.*;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.reactive.BindingContext;

import org.junit.jupiter.api.*;

import mockit.*;

import static org.springframework.data.domain.Sort.Direction.*;
import static guru.mikelue.springframework.data.web.MockServerWebExchangeUtils.*;

public class ReactiveSortParamResolverMockTest {
	@Mocked
	private BindingContext mockBindCtx;

	private ReactiveSortParamResolver testedResolver = new ReactiveSortParamResolver();

	public ReactiveSortParamResolverMockTest() {}

	private final static String SAMPLE_SORT = "name:asc,age:desc,phone";

	/**
	 * Tests the processing for {@link SortDefault}.
	 */
	@Test
	void resolveWithSortDefault()
	{
		/**
		 * Use the settings provided by @SortDefault
		 */
		var testedSort = testedResolver.resolveArgumentValue(
			getMethodParameter(1), mockBindCtx, EmptyWebExchange
		);

		assertThat(testedSort)
			.isEqualTo(
				Sort.by(Order.desc("r1"), Order.desc("r2"))
			);
		// :~)
	}

	/**
	 * Tests the using of "direction" on {@link SortDefault}.
	 */
	@Test
	void resolveWithSortDefaultDirection()
	{
		var httpHeaders = new HttpHeaders();
		httpHeaders.add(ReactiveSortParamResolver.DEFAULT_PARAM_NAME_PAGE_SORT, "k1:asc,k2");

		/**
		 * Use the only the direction provided by @SortDefault
		 */
		var testedSort = testedResolver.resolveArgumentValue(
			getMethodParameter(1), mockBindCtx,
			build(httpHeaders, EmptyMultiValueMap)
		);

		assertThat(testedSort)
			.isEqualTo(
				Sort.by(Order.asc("k1"), Order.desc("k2"))
			);
		// :~)
	}

	/**
	 * Tests the resolving by default fallback of sort.
	 */
	@Test
	void resolveArgumentValueByDefaultFallback()
	{
		var testedSort = testedResolver.resolveArgumentValue(
			getMethodParameter(0), mockBindCtx,
			EmptyWebExchange
		);

		assertThat(testedSort)
			.extracting("unsorted", BOOLEAN)
			.isTrue();
	}

	/**
	 * Tests the resolving by HTTP header.
	 */
	@Test
	void resolveArgumentValueByHeader()
	{
		var httpHeaders = new HttpHeaders();
		httpHeaders.add(ReactiveSortParamResolver.DEFAULT_PARAM_NAME_PAGE_SORT, SAMPLE_SORT);
		var notUsedQueryString = new LinkedMultiValueMap<String, String>();
		notUsedQueryString.add(ReactiveSortParamResolver.DEFAULT_PARAM_NAME_PAGE_SORT, "n1,n2");

		var testedSort = (Sort)testedResolver.resolveArgumentValue(
			getMethodParameter(0), mockBindCtx,
			build(
				httpHeaders, notUsedQueryString
			)
		);

		assertSampleSort(testedSort);
	}

	/**
	 * Tests the resolving by query string.
	 */
	@Test
	void resolveArgumentValueByQueryString()
	{
		var fakeQueryString = new LinkedMultiValueMap<String, String>(2);
		fakeQueryString.add(ReactiveSortParamResolver.DEFAULT_PARAM_NAME_PAGE_SORT, SAMPLE_SORT);

		var testedSort = (Sort)testedResolver.resolveArgumentValue(
			getMethodParameter(0), mockBindCtx,
			build(
				EmptyMultiValueMap,
				fakeQueryString
			)
		);

		assertSampleSort(testedSort);
	}

	private final static void assertSampleSort(Sort testedSort)
	{
		assertThat(testedSort)
			.containsExactly(
				new Sort.Order(ASC, "name"),
				new Sort.Order(DESC, "age"),
				new Sort.Order(ASC, "phone")
			);
	}

	static void sampleMethodParameter(
		Sort sort1,
		@SortDefault(sort={ "r1", "r2" }, direction=DESC)
		Sort sort2
	) {}

	private MethodParameter getMethodParameter(int paramIndex)
	{
		return new MethodParameter(
			ReflectionUtils.findMethod(
				getClass(), "sampleMethodParameter",
				Sort.class, Sort.class
			),
			paramIndex
		);
	}
}
