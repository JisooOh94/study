package 싱글턴_보장;

public class Elvis_Generic_Singleton<T> {
    private static Elvis_Generic_Singleton<Object> elvis = new Elvis_Generic_Singleton<>();
    private Elvis_Generic_Singleton(){};

    public static <V> Elvis_Generic_Singleton<V> getInstance() {
        return (Elvis_Generic_Singleton<V>)elvis;
    }
}
