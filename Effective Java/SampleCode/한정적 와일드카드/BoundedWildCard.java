import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class BoundedWildCard {
	public class Stack<E> {
		private E[] elements;
		int size = 0;

		public Stack(int initialSize) {
			elements = (E[]) new Object[initialSize];
		}

		public void push(E elem) {
			expandWhenFull();

			elements[++size] = elem;
		}

		public E pop() {
			if(isEmpty()) {
				return null;
			}

			E data = elements[size];
			elements[size--] = null;

			return data;
		}

		public void pushAll(List<E> list) {
			for(E elem : list) {
				push(elem);
			}
		}

		public void popAndPut(Collection<E> collection) {
			collection.add(pop());
		}

		public void expandWhenFull() {
			if(size + 1 == elements.length) {
				elements = Arrays.copyOf(elements, elements.length * 2);
			}
		}

		public boolean isEmpty() {
			return elements[0] == null;
		}
	}
	@Test
	public void test() {
		Number num;
		Integer integer;

	}
}
