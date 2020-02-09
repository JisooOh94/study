import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class ChildParent {
	public class Parent {
		public List<Integer> getRangeList(int min, int max) {
			List<Integer> list = new LinkedList<>();
			for(int i = min; i <= max; i++) {		//만약 max값까지 포함하게 수정한다면?
				list.add(i);
			}
			return list;
		}
	}

	public class Child extends Parent{
		public int getSumOfRange(int min, int max) {
			List<Integer> rangeList = getRangeList(min, max);
			return rangeList.stream().mapToInt(Integer::intValue).sum();
		}
	}


	@Test
	public void test() {
		Child child = new Child();
		System.out.println(child.getSumOfRange(0,5));
	}
}
