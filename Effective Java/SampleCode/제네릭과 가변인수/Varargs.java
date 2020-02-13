import java.util.List;

public class Varargs {
	public <T> T[] toArray(T... args) {
		return args;
	}

	public <T> void doSomething(List<T> list) {
		T[] arr = toArray(list.get(0), list.get(1), list.get(2));
	}
}
