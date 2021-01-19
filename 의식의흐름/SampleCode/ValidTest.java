import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.junit.Test;

public class ValidTest {
	public class Foo {
		@AssertFalse
		public boolean booleanVar_False;
		@AssertTrue
		public boolean booleanVar_True;
		@DecimalMin(value = "5")
		public int intVar_DecimalMin;
		@DecimalMax(value = "10")
		public int intVar_DecimalMax;
		@Digits(integer = 2, fraction = 2)
		public double doubleVar_Digits;
		@Future
		public Date date_Future;
		@Past
		public Date date_Past;
		@Min(value = 5)
		public int intVar_Min;
		@Max(value = 5)
		public int intVar_Max;
		@NotNull
		public String strVar_NotNull;
		@Null
		public String strVar_Null;
		@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
		public String strVar_Pattern;
		@Size(min = 5, max = 10)
		public int[] arrVar_Size;

		public Foo(boolean booleanVar_False, boolean booleanVar_True, int intVar_DecimalMin, int intVar_DecimalMax, double doubleVar_Digits, Date date_Future, Date date_Past, int intVar_Min, int intVar_Max, String strVar_NotNull, String strVar_Null, String strVar_Pattern, int[] arrVar_Size) {
			this.booleanVar_False = booleanVar_False;
			this.booleanVar_True = booleanVar_True;
			this.intVar_DecimalMin = intVar_DecimalMin;
			this.intVar_DecimalMax = intVar_DecimalMax;
			this.doubleVar_Digits = doubleVar_Digits;
			this.date_Future = date_Future;
			this.date_Past = date_Past;
			this.intVar_Min = intVar_Min;
			this.intVar_Max = intVar_Max;
			this.strVar_NotNull = strVar_NotNull;
			this.strVar_Null = strVar_Null;
			this.strVar_Pattern = strVar_Pattern;
			this.arrVar_Size = arrVar_Size;
		}

		public Foo clone() {
			return new Foo(this.booleanVar_False, this.booleanVar_True, this.intVar_DecimalMin, this.intVar_DecimalMax, this.doubleVar_Digits, this.date_Future, this.date_Past, this.intVar_Min, this.intVar_Max, this.strVar_NotNull, this.strVar_Null, this.strVar_Pattern, this.arrVar_Size);
		}
	}

	private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private static Validator validator = factory.getValidator();

	private void paramChkMethod(Foo foo) {
		Set<ConstraintViolation<Foo>> violations = validator.validate(foo);
		violations.forEach(this::printViolationCause);
	}

	private <T> void printViolationCause(ConstraintViolation<T> violation) {
		System.out.println(violation.getMessage());
	}

	@Test
	public void Test() {
		Date tomorrow = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
		Date yesterdate = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
		Foo defaultObj = new Foo(false, true, 5, 10, 11.11, tomorrow, yesterdate, 5, 5, "", null, "2020-11-11",new int[7]);

		Foo assertFalseObj = defaultObj.clone();
		Foo assertTrueObj = defaultObj.clone();
		Foo decimalMin = defaultObj.clone();
		Foo decimalMax = defaultObj.clone();
		Foo digits = defaultObj.clone();
		Foo future = defaultObj.clone();
		Foo past = defaultObj.clone();
		Foo intMin = defaultObj.clone();
		Foo intMax = defaultObj.clone();
		Foo strNotNull = defaultObj.clone();
		Foo strNull = defaultObj.clone();
		Foo pattern = defaultObj.clone();
		Foo arrSize = defaultObj.clone();

		paramChkMethod(defaultObj);

		assertFalseObj.booleanVar_False = true;
		paramChkMethod(assertFalseObj);

		assertTrueObj.booleanVar_True = false;
		paramChkMethod(assertTrueObj);

		decimalMax.intVar_DecimalMax = 100;
		paramChkMethod(decimalMax);

		decimalMin.intVar_DecimalMin = 0;
		paramChkMethod(decimalMin);

		digits.doubleVar_Digits = 1111.1111;
		paramChkMethod(digits);

		future.date_Future = new Date(1);
		paramChkMethod(future);

		past.date_Past = new Date(10000000000000L);
		paramChkMethod(past);

		intMax.intVar_Max = 100;
		paramChkMethod(intMax);

		intMin.intVar_Min = 0;
		paramChkMethod(intMin);

		strNotNull.strVar_NotNull = null;
		paramChkMethod(strNotNull);

		strNull.strVar_Null = "A";
		paramChkMethod(strNull);

		pattern.strVar_Pattern = "2020/02/20";
		paramChkMethod(pattern);

		arrSize.arrVar_Size = new int[1];
		paramChkMethod(arrSize);
	}
}
