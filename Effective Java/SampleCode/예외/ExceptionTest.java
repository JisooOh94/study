import java.security.InvalidParameterException;

import org.junit.Test;

public class ExceptionTest {
	private void exceptionMethod() {
		throw new InvalidParameterException("Lower level exception");
	}

	@Test
	public void exceptionChainTest() {
		try {
			exceptionMethod();
		} catch (InvalidParameterException e) {
			throw new RuntimeException("High level exception",e);
		}
	}

	@Test
	public void exceptionChainTest_2() {
		try {
			exceptionMethod();
		} catch (InvalidParameterException e) {
			RuntimeException ex = new RuntimeException("High level exception");
			ex.initCause(e);

			throw ex;
		}
	}
}
