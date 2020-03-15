import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class OverloadingTest {
	public class Parent {
		public void listMethod(List<String> list) {
			System.out.println("Parent listMethod called");
		}
	}

	public class Child extends Parent {
		@Override
		public void listMethod(List<String> list) {
			System.out.println("Child LinkedListMethod called");
		}
	}

	private void listMethod(List<String> list) {
		System.out.println("ListMethod called");
	}

	private void listMethod(ArrayList<String> arrayList) {
		System.out.println("ArrayListMethod called");
	}

	private void listMethod(LinkedList<String> linkedList) {
		System.out.println("LinkedListMethod called");
	}
	@Test
	public void test() {
		List<String> arrayList = new ArrayList<>();
		List<String> linkedList = new LinkedList<>();

		listMethod(arrayList);
		listMethod(linkedList);

		Parent parent = new Child();
		parent.listMethod(linkedList);
	}
}
