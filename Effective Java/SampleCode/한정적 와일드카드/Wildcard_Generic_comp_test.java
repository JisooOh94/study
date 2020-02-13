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
}
