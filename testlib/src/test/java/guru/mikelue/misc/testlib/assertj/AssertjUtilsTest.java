package guru.mikelue.misc.testlib.assertj;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.*;

public class AssertjUtilsTest {
    @SuppressWarnings("unused")
    private final static Logger logger = LoggerFactory.getLogger(AssertjUtilsTest.class);

    public AssertjUtilsTest() {}

    /**
     * Tests the conversion of lambda interface
     */
    @Test
    void asThrowingCallable() throws Throwable
    {
        var valueHolder = new ValueHolder();

        var testedCallableOfAssertJ = AssertjUtils.asThrowingCallable(
            () -> valueHolder.value = 1
        );

        testedCallableOfAssertJ.call();

        assertThat(valueHolder.value).isEqualTo(1);
    }
}

class ValueHolder {
    int value = 0;
}
