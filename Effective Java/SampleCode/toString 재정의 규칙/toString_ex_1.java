import org.junit.Assert;
import org.junit.Test;

public class toString_ex_1 {
	public class Foo {
		private int intVar;
		private String stringVar;
		private double doubleVar;

		public Foo(int intVar, String stringVar, double doubleVar) {
			this.intVar = intVar;
			this.stringVar = stringVar;
			this.doubleVar = doubleVar;
		}
	}

	public class Bar {
		private int intVar;
		private String stringVar;
		private double doubleVar;

		public Bar(int intVar, String stringVar, double doubleVar) {
			this.intVar = intVar;
			this.stringVar = stringVar;
			this.doubleVar = doubleVar;
		}

		@Override
		public String toString() {
			return "Bar - intVar : " + intVar + ", stringVar : " + stringVar + ", doubleVar : " + doubleVar;
		}
	}

	@Test
	public void testFoo() {
		Foo foo_1 = new Foo(1, "A", 0.1);
		Foo foo_2 = new Foo(2, "B", 1.1);

		Assert.assertEquals(foo_1, foo_2);
	}

	@Test
	public void testBar() {
		Bar bar_1 = new Bar(1, "A", 0.1);
		Bar bar_2 = new Bar(2, "B", 1.1);

		Assert.assertEquals(bar_1, bar_2);
	}

}
