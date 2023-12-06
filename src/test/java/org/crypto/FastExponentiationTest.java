package org.crypto;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class FastExponentiationTest {

    @Test
    public void fastExponentiationTest() {
        //assertEquals(FastExponentiation.fastExponentiation(2, 100, 71), 20);
        //
        assertEquals(FastExponentiation.fastExponentiation(50, 35, 71), 1);
    }

    @Test
    public void fastExponentiationUsingBigIntegers() {
        assertEquals(FastExponentiation.fastExponentiation(BigInteger.valueOf(50), BigInteger.valueOf(35), BigInteger.valueOf(71)), BigInteger.ONE);
    }

    @Test
    public void fastExponentiationCache() {
        BigInteger res = FastExponentiation.fastExponentiation(BigInteger.valueOf(9812), BigInteger.valueOf(7915), BigInteger.valueOf(12091));
        assertTrue(FastExponentiation.store.containsKey(String.format("%s|%s|%s", 9812, 7915, 12091)));
        assertEquals(res, FastExponentiation.store.get(String.format("%s|%s|%s", 9812, 7915, 12091)));
    }


    @Test
    public void generateExponentsTest() {
        FastExponentiation.generateExponentsOfNumberNUptoK(9812, 7915, 12091);
    }
}