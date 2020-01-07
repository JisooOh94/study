package 싱글턴_보장;

public class Elvis_v2 {
    private static Elvis_v2 elvis = new Elvis_v2();
    private Elvis_v2() {}

    public static Elvis_v2 getInstance() { return elvis; }

    public void action(){};
}
