import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.junit.Test;

public class AnnotationTest {
	@Retention(RetentionPolicy.CLASS)
	@Target(ElementType.METHOD)
	public @interface TestAnnotation {

	}
	@Test
	public void test(String className) throws ClassNotFoundException {
		Class<?> targetClass = Class.forName(className);
		for(Method m : targetClass.getDeclaredMethods()) {
		}
	}
}
