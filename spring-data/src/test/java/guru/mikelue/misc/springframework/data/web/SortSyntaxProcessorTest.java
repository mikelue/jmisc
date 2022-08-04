package guru.mikelue.misc.springframework.data.web;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Sort.Direction;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.data.domain.Sort.Direction.*;

public class SortSyntaxProcessorTest {
	private SortSyntaxProcessor testedProcessor = newProcessor();

	public SortSyntaxProcessorTest() {}

	/**
	 * Tests the processing of {@link Sort} from sort syntax.
	 */
	@ParameterizedTest
	@MethodSource
	void buildSort(
		String sampleProperty, Direction defaultDirection,
		Sort expectedSort
	) {
		assertThat(
			testedProcessor.buildSort(
				sampleProperty, defaultDirection
			)
		)
			.isEqualTo(expectedSort);
	}
	static Arguments[] buildSort()
	{
		return new Arguments[] {
			arguments(
				"name,age", ASC,
				Sort.by(Order.asc("name"), Order.asc("age"))
			),
			arguments(
				"v1", DESC,
				Sort.by(Order.desc("v1"))
			),
			arguments(
				"v2:desc", ASC,
				Sort.by(Order.desc("v2"))
			),
			arguments( // Empty properties
				"v1,,,v3", ASC,
				Sort.by(Order.asc("v1"), Order.asc("v3"))
			),
			arguments( // Fallback with empty syntax
				"", ASC,
				Sort.unsorted()
			),
		};
	}

	/**
	 * Tests the processing of property syntax to {@link Sort.Order} in sort syntax.
	 */
	@ParameterizedTest
	@MethodSource
	void propertyToOrder(
		String sampleProperty, Direction defaultDirection,
		Order expectedOrder
	) {
		var testedProcessor = newProcessor();

		assertThat(
			testedProcessor.propertyToOrder(
				sampleProperty, defaultDirection
			)
		)
			.isEqualTo(expectedOrder);
	}
	static Arguments[] propertyToOrder()
	{
		return new Arguments[] {
			arguments(
				"name:asc", ASC,
				Order.asc("name")
			),
			arguments(
				"name", DESC,
				Order.desc("name")
			),
			arguments(
				"name:desc", ASC,
				Order.desc("name")
			),
			arguments( // Empty direction
				"name:", ASC,
				Order.asc("name")
			),
			arguments( // Cannot be recognized direction
				"name:non", DESC,
				Order.desc("name")
			),
		};
	}

	private static SortSyntaxProcessor newProcessor()
	{
		return new SortSyntaxProcessor(
			ReactiveSortParamResolver.DEFAULT_PROP_DELIMITER,
			ReactiveSortParamResolver.DEFAULT_SORT_DELIMITER,
			Sort.unsorted()
		);
	}
}
