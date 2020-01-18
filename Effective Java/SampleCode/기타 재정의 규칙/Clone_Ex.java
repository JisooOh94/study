import org.junit.Test;

public class Clone_Ex {
    public class Foo implements Cloneable {
        int intVar;
        String strVar;

        public Foo(int intVar, String strVar) {
            this.intVar = intVar;
            this.strVar = strVar;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Foo foo = new Foo(1, "1");
        Foo clonedFoo = (Foo)foo.clone();
        System.out.println(foo.equals(clonedFoo));
    }
}
