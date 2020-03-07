import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;

import org.junit.Test;

public class Functional_Intereface {
	private final static Logger logger = Logger.getLogger(Functional_Intereface.class.getName());

	@FunctionalInterface public interface SquareCalculator<T> {
		public abstract T doSquare(T value);
	}

	public class Sample {
		UnaryOperator<Integer> operator;

		public Sample(UnaryOperator<Integer> operator) {
			this.operator = operator;
		}

		public int getSquaredNum(int num) {
			return operator.apply(num);
		}
	}

	public class Sample_2 {
		SquareCalculator<Integer> calculator;

		public Sample_2(SquareCalculator<Integer> calculator) {
			this.calculator = calculator;
		}

		public int getSquaredNum(int num) {
			return calculator.doSquare(num);
		}
	}

	@Test
	public void test() {
		Sample sample = new Sample(num -> num * num);
		int num = sample.getSquaredNum(5);

		logger.info(String.valueOf(num));
	}

	@Test
	public void test_2() {
		Sample_2 sample = new Sample_2(num -> num * num);
		int num = sample.getSquaredNum(5);

		logger.info(String.valueOf(num));
	}
}
