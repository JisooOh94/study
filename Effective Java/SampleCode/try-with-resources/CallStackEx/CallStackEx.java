package CallStackEx;

import java.io.IOException;
import java.util.logging.Logger;

public class CallStackEx implements AutoCloseable {
	private final static Logger logger = Logger.getLogger(CallStackEx.class.getName());
	public void doSomething() {
		throw new FirstError();
	}

	@Override
	public void close() {
		throw new SecondError();
	}

	private static void testMethod_1() {
		CallStackEx myResource = new CallStackEx();
		try {
			myResource.doSomething();
		} finally {
			myResource.close();        //FirstError에 대한 정보가 사라짐
		}
	}

	private static void testMethod_2() {
		CallStackEx myResource = new CallStackEx();
		try {
			myResource.doSomething();
		} finally {
			try {
				myResource.close();
			} catch (Exception e) {
				logger.severe(e.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		testMethod_2();
	}
}