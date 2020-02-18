import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class Wildcard_Generic_comp {
	public static <T> void get_Generic(List<T> list, int idx) {
		System.out.println("Generic");
	}

	public static void get_Wildcard(List<?> list, int idx) {
		System.out.println("Wildcard");
	}

	public static <T, E> void get_Generic(List<T> list_1, List<E> list_2, int idx) {
		System.out.println("Generic");
	}

	public static void get_Wildcard(List<?> list_1, List<?> list_2, int idx) {
		System.out.println("Generic");
	}

	public static <T> void get_Generic_add(List<T> from, List<T> to, int idx) {
		System.out.println("Generic");
		to.add(from.get(idx));
	}

	public static void get_Wildcard_add(List<?> from, List<?> to, int idx) {
		System.out.println("Wildcard");
		//to.add(from.get(idx));
		to.add(null);
	}
}
