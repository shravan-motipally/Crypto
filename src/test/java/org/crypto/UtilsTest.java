package org.crypto;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static java.math.BigInteger.*;
import static org.crypto.Utils.randomBigIntegerWithin;
import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {

    @Test
    public void generateLargeNumberLessThanGivenNumber() {
            assertTrue(randomBigIntegerWithin(TWO.pow(16)).compareTo(BigInteger.valueOf(100000004449L)) < 0);
    }

    @Test
    public void generateNumberThatIsPositiveNonZero() {
        BigInteger randomVal;
        for (int i = 0; i <= 10; i++) {
            randomVal = randomBigIntegerWithin(valueOf(5));
            assertNotEquals(ZERO, randomVal);
            assertTrue(randomVal.compareTo(ZERO) > 0);
        }
    }

    @Test
    public void ensureEdgeCasesWorkForZeroAndOne() {
        assertEquals(ONE, randomBigIntegerWithin(ONE));
        assertThrows(RuntimeException.class, () -> {
            randomBigIntegerWithin(ZERO);
        });
    }
}