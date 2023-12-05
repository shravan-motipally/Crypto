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
    public void generateExponentsTest() {
        FastExponentiation.generateExponentsOfNumberNUptoK(9812, 7915, 12091);
    }
}