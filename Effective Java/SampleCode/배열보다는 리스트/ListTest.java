import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ListTest {
	@Test
	public void test() {
		Object[] objArr = new Long[5];
		char[] charArr = new char[]{'a','b'};
		Integer[] arr = new Integer[10];
		Assert.assertTrue(arr instanceof Object[]);
		objArr[0] = charArr;
	}
}
