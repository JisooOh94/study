package Custom_Serialization;

import org.junit.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TestClass {
    public class List<T> implements Serializable {
        private class Entry<T> implements Serializable {
            /**
             * @serial data
             */
            T data;
            Entry next;
            Entry prev;
        }
        private int size;
        private Entry<T> head;
    }


    public class List_Upgrade<T> implements Serializable {
        private static final long serialVersionUID = 1234556789L;
        private class Entry<T> {
            /**
             * @serial data field
             */
            T data;
            Entry next;
            Entry prev;
        }

        /**
         * @serial size of list
         */
        private transient int size;
        private transient Entry<T> head;

        private void WriteObejct(ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.write(this.size);

            for(Entry e = head; e != null ; e = e.next) {
                stream.write((byte[]) e.data);
            }
        }
    }

}
