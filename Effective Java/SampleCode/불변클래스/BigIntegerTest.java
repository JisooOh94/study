import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class BigIntegerTest {
	@Test
	public void test() {
		BigInteger bigInteger = new BigInteger(new byte[]{1,0});
		System.out.println(bigInteger);

		BigInteger result = bigInteger.flipBit(3);
		System.out.println(result);
	}
}
