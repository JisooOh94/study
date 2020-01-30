import java.util.function.UnaryOperator;

import org.junit.Assert;
import org.junit.Test;

public class StaticFactory_2 {
	private static UnaryOperator<Object> IDENTITY_FN = t -> t;

	@java.lang.SuppressWarnings("unchecked")
	public static <T> UnaryOperator<T> identityFunction() {
		return(UnaryOperator<T>)IDENTITY_FN;
	}

	@Test
	public void test() {
		String str_1 = "A";
		UnaryOperator<String> identityFunction_String = identityFunction();

		String str_2 = identityFunction_String.apply(str_1);

		Assert.assertEquals(str_1, str_2);

		int num_1 = 1;
		UnaryOperator<Integer> identityFunction_Integer = identityFunction();

		int num_2 = identityFunction_Integer.apply(num_1);

		Assert.assertEquals(num_1, num_2);
	}
}
