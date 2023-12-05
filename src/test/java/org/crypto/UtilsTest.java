package org.crypto;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static java.math.BigInteger.TWO;
import static org.crypto.Utils.randomBigIntegerWithin;
import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {

    @Test
    public void generateLargeNumberLessThanGivenNumber() {
            assertTrue(randomBigIntegerWithin(TWO.pow(16)).compareTo(BigInteger.valueOf(100000004449L)) < 0);

    }
}