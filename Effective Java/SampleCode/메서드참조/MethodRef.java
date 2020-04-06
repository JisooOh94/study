import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.Test;

public class MethodRef {
	public int sum(int a, int b) {
		return a + b;
	}

	public int sumInt(MethodRef obj, int a, int b) {
		return obj.sum(a,b);
	}

	@Test
	public void test() {
		Map<String, Integer> map = new HashMap<>();

		map.merge("key", 1, this::sum);
		List<String> list = null;
		Collections.sort(list, Comparator.comparingInt(String::length));
	}

	public interface Calculator { public abstract int sum(int a, int b); }

	public class CalculatorImpl implements Calculator {
		public int sum(int a, int b) { return a + b; }
	}

	@Test
	public void instanceMethodRefTest() {
		Integer integer = 5;
		int result = integer.sum(1, 3);
		System.out.println(result);
		List<Integer> list = new LinkedList<>();
	}
}
