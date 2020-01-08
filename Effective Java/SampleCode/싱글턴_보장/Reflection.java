package 싱글턴_보장;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Reflection {
    /*
    public class Elvis_v1 {
        public static 싱글턴_보장.Elvis_v1 elvis = new 싱글턴_보장.Elvis_v1();
        private Elvis_v1() {}

        public void action(){};
    }
     */

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class userClass = Elvis_v1.class;

        Constructor cs = userClass.getDeclaredConstructor();
        cs.setAccessible(true); // 중요. access 가능하도록 변경

        Elvis_v1 user = (Elvis_v1)cs.newInstance();

        System.out.println(user.toString());
    }
}
