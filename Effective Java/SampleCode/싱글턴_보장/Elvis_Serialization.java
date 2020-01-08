package 싱글턴_보장;

import java.io.Serializable;

public class Elvis_Serialization implements Serializable {
    private static Elvis_Serialization elvis = new Elvis_Serialization();

    private transient String var;

    public Object readResolve() {
        return elvis;
    }
}
