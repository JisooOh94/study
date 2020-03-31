import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

public class Varargs {
	public enum TestEnum {
		FIRST(1, "1"), SECOND(2, "2");

		private int intVal;
		private String strVal;

		TestEnum(int intVal, String strVal) {
			this.intVal = intVal;
			this.strVal = strVal;
		}
	}

	public <T> T[] toArray(T... args) {
		return args;
	}

	public <T> void doSomething(List<T> list) {
		T[] arr = toArray(list.get(0), list.get(1), list.get(2));
	}

	public <T> List<T> foo(T var) {
		return Arrays.asList(var);
	}

	public <T> void ellipsis(T... args) {
		for(int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
		}
	}

	public <T> T[] pickTwo(T a, T b, T c) {
		return toArray(a, b);
	}

	public void dangerous(List<String>... stringArr) {
		List<Integer> list = Arrays.asList(1,2);
		Object[] objList = stringArr;
		objList[0] = list;
	}

	@Test
	public void test() {
		List list = foo(1);
		list.add(false);
	}

	@Test
	public void ellipsisTest() {
		List<String>[] stringArr = null;
		stringArr[0] = Arrays.asList("a");
		Object[] objList = stringArr;
		List<Integer> list = Arrays.asList(1,2);
		objList[0] = list;
		//arr = pickTwo(1,2,3);
	}

	@Test
	public void ellipsisTest2() {
		List list = new ArrayList();
		list.add("A");
		list.add(1);
		list.add(TestEnum.FIRST);

		Object[] objArr = new Object[3];
		objArr[0] = "A";
		objArr[1] = 1;
		objArr[2] = TestEnum.FIRST;

		TestEnum[] arr = new TestEnum[3];
		arr[0] = TestEnum.FIRST;
		//arr[1] = 1;

		for(Object var : list) {
			System.out.println(var);
		}

		for(int i = 0; i< 3; i++) {
			System.out.println(objArr[i]);
		}

		//Raw 타입과 제네릭 가변인수 동시 사용시, 어느것 하나 소거인게 없어 컴파일 타임에 타입 안정성 체크 불가능
		List arrList = Arrays.asList(1,2,3);
		arrList.add(TestEnum.FIRST);

		Object[] objArr2 = ArrayUtils.toArray(1,2,3);
		objArr2[2] = TestEnum.FIRST;
	}
}
