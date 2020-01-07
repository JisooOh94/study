package 싱글턴_보장;

public class Elvis_v1 {
    public static Elvis_v1 elvis = new Elvis_v1();
    private Elvis_v1() {}

    public void action(){};
}
