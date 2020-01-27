import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class InstanceOf {
	@Test
	public void test() {
		List<String> strList = new ArrayList<>();

//		if(strList instanceof List<String>) {
//			System.out.println("String List");
//		}
		if(strList instanceof List<?>) {
			System.out.println("String List");
		}
		if(strList instanceof List) {
			System.out.println("String List");
		}
	}
}
