import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class Wildcard_Generic_comp_test {
	@Test
	public void test() {
		List<Integer> list = Arrays.asList(1,2,3,4);
		Wildcard_Generic_comp.get_Generic(list, 0);
		Wildcard_Generic_comp.get_Wildcard(list, 0);
	}

	public static <T> List<? extends T> union(List<? extends T> list1, List<? extends T> list2) {
		return list1;
	}

	@Test
	public void test2() {
		List<Integer> list_1 = Arrays.asList(1,2,3);
		List<Double> list_2 = Arrays.asList(1.1, 1.2, 1.3);
		List<? extends Number> resultList = union(list_1, list_2);
	}
}
