package 싱글턴_보장;

public class Elvis_Generic<T> {
    T var;

    public static <V> Elvis_Generic<V> make() {
        return new Elvis_Generic<V>();
    }
}
