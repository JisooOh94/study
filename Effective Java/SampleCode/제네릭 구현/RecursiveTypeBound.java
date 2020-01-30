import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class RecursiveTypeBound {
	public static <E extends Comparable<E>> E max(Collection<E> c) {
		return null;
	}

	public class Parent implements Comparable<Parent> {
		@Override
		public int compareTo(Parent o) {
			return 0;
		}
	}

	public class Child extends Parent {}

	public class Sibling implements Comparable<String> {
		@Override
		public int compareTo(String str) {
			return 0;
		}
	}

	@Test
	public void test() {
		List<Parent> parentList = Arrays.asList(new Parent());
		List<Child> childList = Arrays.asList(new Child());
		List<Sibling> siblingList = Arrays.asList(new Sibling());

		Parent parent = max(parentList);

		//Child child = max(childList);
		//Sibling sibling = max(siblingList);
	}
}
