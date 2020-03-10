import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class ParameterValidation {
	private void testMethod(int a, String b) {
		assert a != 0;
		assert StringUtils.isNotEmpty(b);
	}

	private void nullChkTest(String args) {
		String str = Objects.requireNonNull(args);
	}

	@Test
	public void test() {
		testMethod(0, "");
	}

	@Test
	public void test_2() {
		nullChkTest(null);
	}
}
