import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class WildCard {
	public void printElemRaw(List list) {
		for(Object obj : list) {
			System.out.print(obj);
		}
		list.add(1L);
	}

	public void printElemWildCard(List<?> list) {
		for(Object obj : list) {
			System.out.print(obj);
		}
		//list.add(1L);
	}

	@Test
	public void test() {
		List<String> stringList = Arrays.asList("A", "B", "C");
		List<Double> doubleList = Arrays.asList(1.1,1.2,1.3);

		printElemRaw(stringList);
		printElemWildCard(stringList);

		printElemRaw(doubleList);
		printElemWildCard(doubleList);
	}
}
