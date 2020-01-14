package 싱글턴_보장;

import java.util.function.Supplier;

public class Main {
    public static void main(String[]args) {
        Elvis_Generic_Singleton<String> elivs = Elvis_Generic_Singleton.getInstance();
        elivs.setVar("abc");
        System.out.println(elivs.getVar());

        Elvis_Generic_Singleton<Integer> elvis_Integer = Elvis_Generic_Singleton.getInstance();
        System.out.println(elvis_Integer.getVar());
        elvis_Integer.setVar(5);
        System.out.println(elvis_Integer.getVar());
    }
}