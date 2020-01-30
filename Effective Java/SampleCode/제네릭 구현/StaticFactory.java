import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class StaticFactory {
	private static List<Object> objList = new ArrayList<>();

	@java.lang.SuppressWarnings("unchecked")
	public static <T> List<T> getList() {
		return (List<T>)objList;
	}

	@Test
	public void test() {
		List<String> strList = getList();
	}
}
