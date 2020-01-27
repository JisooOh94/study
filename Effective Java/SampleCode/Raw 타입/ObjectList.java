import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ObjectList {
	public class TestClass {
	}

	public void addElement(List<Object> list, Object obj) {
		list.add(obj);
	}

	@Test
	public void test() {
		List<String> stringList = new ArrayList<>();
		List<Object> objList = new ArrayList<>();

		//addElement(stringList, "B");
		//addElement(objList, "B");
	}
}
