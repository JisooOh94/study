import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

public class Varargs {
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
		stringArr[0] = list;
		//arr = pickTwo(1,2,3);
	}
}
