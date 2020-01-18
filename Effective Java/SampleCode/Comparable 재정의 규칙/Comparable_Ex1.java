import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class Comparable_Ex1 {
	public class Foo implements Comparable<Foo> {
		private int var;

		public Foo(int var) {
			this.var = var;
		}

		public int getVar() {
			return var;
		}

		public int compareTo(Foo obj) {
			return Integer.compare(this.var, obj.getVar());
		}

		@Override
		public String toString() {
			return String.valueOf(var);
		}
	}

	@Test
	public void test() {
		Foo foo1 = new Foo(1);
		Foo foo2 = new Foo(10);
		Foo foo3 = new Foo(100);

		System.out.println(foo1.compareTo(foo2));

		System.out.println("---------TreeMap--------------");
		TreeMap<Foo, String> treeMap = new TreeMap<>();
		treeMap.put(foo3, "foo3");
		treeMap.put(foo2, "foo2");
		treeMap.put(foo1, "foo1");

		treeMap.entrySet().forEach(entry -> {
			System.out.println(String.format("Key : %s , Value : %s", entry.getKey(), entry.getValue()));
		});

		System.out.println("---------HashMap--------------");
		Map<Foo, String> normalMap = new HashMap<>();
		normalMap.put(foo3, "foo3");
		normalMap.put(foo2, "foo2");
		normalMap.put(foo1, "foo1");

		normalMap.entrySet().forEach(entry -> {
			System.out.println(String.format("Key : %s , Value : %s", entry.getKey(), entry.getValue()));
		});

		System.out.println("---------TreeSet--------------");
		TreeSet<Foo> treeSet = new TreeSet<>();
		treeSet.add(foo3);
		treeSet.add(foo2);
		treeSet.add(foo1);

		treeSet.forEach(foo -> {
			System.out.println(foo);
		});

		System.out.println("---------Set--------------");
		Set<Foo> set = new HashSet<>();
		set.add(foo3);
		set.add(foo2);
		set.add(foo1);

		set.forEach(foo -> {
			System.out.println(foo);
		});


		System.out.println("---------List--------------");
		List<Foo> list = new LinkedList<>();
		list.add(foo3);
		list.add(foo2);
		list.add(foo1);

		list.forEach(foo -> {
			System.out.print(foo + " > ");
		});

		System.out.println("\n---------Sorted List--------------");
		Collections.sort(list);

		list.forEach(foo -> {
			System.out.print(foo + " > ");
		});
	}
}
