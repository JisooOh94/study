import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

public class Comparable_Equals {
	@Test
	public void test() {
		BigDecimal bigDecimal = new BigDecimal("1.0");
		BigDecimal bigDecima2 = new BigDecimal("1.00");

		Map<BigDecimal, String> normalMap = new HashMap<>();
		TreeMap<BigDecimal, String> treeMap = new TreeMap<>();

		normalMap.put(bigDecimal, "bigDecimal_1");
		treeMap.put(bigDecimal, "bigDecimal_1");

		normalMap.put(bigDecima2, "bigDecimal_2");
		treeMap.put(bigDecima2, "bigDecimal_2");

		System.out.println(normalMap);
		System.out.println(treeMap);
	}
}
