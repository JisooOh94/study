import org.junit.Test;

import static org.hamcrest.core.DescribedAs.describedAs;
import static org.hamcrest.core.IsAnything.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

public class MatcherTest {
    @Test
    public void test() {
        int num = 0;
        assertThat(num, anything());
        assertThat(num, is(equalTo(1)));
        assertThat(num, describedAs("value must be 1",equalTo(1)));
    }
}
