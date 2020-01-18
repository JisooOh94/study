import static java.util.Comparator.*;

import java.util.Comparator;

import org.junit.Test;

public class Comparable_Comparator {
	private static final Comparator<PhoneNumber> PHONE_NUMBER_COMPARATOR =
			comparingInt((PhoneNumber phoneNumber) -> phoneNumber.areaCode)
					.thenComparingInt(phoneNumber -> phoneNumber.prefix)
					.thenComparingInt(phoneNumber -> phoneNumber.lineNum);

	public class PhoneNumber implements Comparable<PhoneNumber>{
		private int areaCode;
		private int prefix;
		private int lineNum;

		public PhoneNumber(int areaCode, int prefix, int lineNum) {
			this.areaCode = areaCode;
			this.prefix = prefix;
			this.lineNum = lineNum;
		}

		public int compareTo(PhoneNumber phoneNumber) {
			return PHONE_NUMBER_COMPARATOR.compare(this, phoneNumber);
		}
	}

	@Test
	public void test() {
		PhoneNumber phoneNumber_1 = new PhoneNumber(031,369,0001);
		PhoneNumber phoneNumber_2 = new PhoneNumber(031,369,0002);

		System.out.println(phoneNumber_1.compareTo(phoneNumber_2));
	}
}
