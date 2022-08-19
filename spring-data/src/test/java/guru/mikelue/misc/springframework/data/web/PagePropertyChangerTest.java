package guru.mikelue.misc.springframework.data.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import org.junit.jupiter.api.Test;

public class PagePropertyChangerTest {
	public PagePropertyChangerTest() {}

	/**
	 * Tests the mapping for properties.
	 */
	@Test
	void mapProperty()
	{
		var testedChanger = PagePropertyChanger.from(
			Map.of("c1", "c7", "c2", "c6")
		);

		Pageable testedPageable = PageRequest.of(
			2, 30,
			Sort.by(
				Order.desc("c1"),
				Order.asc("c3"),
				Order.desc("c4")
			)
		);

		var testedSorts = testedChanger.mapProperty(
			testedPageable
		).getSort().toList();

		assertThat(testedSorts)
			.extracting("property")
			.containsExactly("c7", "c3", "c4");

		assertThat(testedSorts.get(0))
			.hasFieldOrPropertyWithValue("direction", Sort.Direction.DESC);
	}
}
