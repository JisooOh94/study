import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

public class Stream {
	@Test
	public void streamTest() {
		List<Integer> nums = Arrays.asList(1,2,3,4,5);
		List<Integer> evenNums = nums.stream().filter(n -> n % 2 == 0).collect(Collectors.toList());
	}

	@Test
	public void test() {
		int result = IntStream.range(0, 1000).sum();
	}
}
