package guru.mikelue.misc.springframework.data.web;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.*;

public class PageableUtilsTest {
	public PageableUtilsTest() {}

	/**
	 * Tests limitations for {@link Pageable}.
	 */
	@ParameterizedTest
	@CsvSource(
		value={
			"1, 10, 10, 20, 1, 10", // Nothing changed
			"10, 100, 5, 50, 5, 50", // Change both of properties to limit
		}
	)
	void LimitPageable(
		int originalPage, int originalSize,
		int limitPage, int limitSize,
		int expectedPage, int expectedSize
	) {
		var sourcePageable = PageRequest.of(
			originalPage, originalSize,
			Sort.by("f1", "f2")
		);
		var testedPageable = PageableUtils.LimitPageable(sourcePageable, limitPage, limitSize);

		assertThat(testedPageable)
			.hasFieldOrPropertyWithValue("pageNumber", expectedPage)
			.hasFieldOrPropertyWithValue("pageSize", expectedSize);

		/**
		 * Asserts the sort content
		 */
		assertThat(testedPageable.getSort())
			.hasSize(2);
		// :~)
	}
}
