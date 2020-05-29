import org.junit.Test;

import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.DescribedAs.describedAs;
import static org.hamcrest.core.IsAnything.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;
import static org.hamcrest.num
import static org.hamcrest.core.Is.is;

import org.hamcrest.core.*;

public class MatcherTest {
    @Test
    public void test() {
        Integer num = 0;
        Integer num_2 = 1;
        Object obj_1 = new Object();
        Object obj_2 = obj_1;
//        assertThat(num, anything());
//        assertThat(num, is(equalTo(1)));
//        assertThat(num, describedAs("value must be 1",equalTo(1)));
//        assertThat(num, allOf(equalTo(2), notNullValue()));
//        assertThat(num, is(nullValue()));
        assertThat(obj_1, sameInstance(obj_2));
    }
}
