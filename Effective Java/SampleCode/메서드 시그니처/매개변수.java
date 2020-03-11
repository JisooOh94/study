import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

public class 매개변수 {
	public static class Param{
		int a;
		String b;
		char c;

		public Param(int a, String b, char c) {
			this.a = a;
			this.b = b;
			this.c = c;
		}
	}

	public void doSomething(int a, String b, char c) {
	}

	public void doSomething(Param p) {
	}

	public void doJobWithClass(HashMap map) {

	}

	public void doJobWithInteface(Map map) {

	}

	public enum Person {
		STUDENT, NOT_STUDENT;
	}

	public void printStudent(Person person) {
		if(person == Person.STUDENT) {
			System.out.println("Student");
		} else if(person == Person.NOT_STUDENT) {
			System.out.println("Not Student");
		}
	}

	public void printStudent(boolean isStudent) {
		if(isStudent) {
			System.out.println("Student");
		} else if(!isStudent) {
			System.out.println("Not Student");
		}
	}

	@Test
	public void test() {
		doSomething(1, "1", '1');
		Param p = new Param(1, "1", '1');
		doSomething(p);
		doSomething(new Param(1, "1", '1'));
	}

	@Test
	public void test2() {
		HashMap<String, Object> hashMap = new HashMap<>();
		TreeMap<String, Object> treeMap = new TreeMap<>();

		doJobWithClass(hashMap);
		//doJobWithClass(treeMap);

		doJobWithInteface(hashMap);
		doJobWithInteface(treeMap);
	}

	@Test
	public void test3() {
		printStudent(Person.STUDENT);
		printStudent(true);
	}
}
