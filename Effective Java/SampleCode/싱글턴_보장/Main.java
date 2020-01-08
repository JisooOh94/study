package 싱글턴_보장;

import java.util.function.Supplier;

public class Main {
    public static void main(String[]args) {
        Elvis_Generic<String> elvis = new Elvis_Generic<String>();
        Elvis_Generic<String> elivs = Elvis_Generic.make();

        Supplier<Elvis_v2> supplier = Elvis_v2::getInstance;
    }
}