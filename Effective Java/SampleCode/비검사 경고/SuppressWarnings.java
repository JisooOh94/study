import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SuppressWarnings {
	@Test
	public void test() {
		@java.lang.SuppressWarnings("unchecked")
		List<String> list = new ArrayList();
		list.add("A");
	}
}
