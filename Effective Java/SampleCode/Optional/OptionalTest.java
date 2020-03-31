import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.junit.Test;

public class OptionalTest {
	private OptionalInt max(List<Integer> list) {
		if(list.isEmpty()) {
			return OptionalInt.empty();
		}
		list.sort(Comparator.comparingInt(Integer::intValue));

		return OptionalInt.of(list.get(list.size() - 1));
	}
	@Test
	public void test() {
		List<Integer> list = IntStream.range(0, 100).boxed().collect(Collectors.toList());
		OptionalInt maxValOptional = max(list);

		int maxVal = maxValOptional.orElse(-1);
	}

	@Test
	public void test_2() {
		List<Integer> list = new LinkedList<>();
		OptionalInt maxValOptional = max(list);

		int gap = 1000 - maxValOptional.getAsInt();
	}
}
