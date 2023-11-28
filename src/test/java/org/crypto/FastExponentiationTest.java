package org.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FastExponentiationTest {

    @Test
    public void fastExponentiationTest() {
        //assertEquals(FastExponentiation.fastExponentiation(2, 100, 71), 20);
        FastExponentiation.fastExponentiation(50, 35, 71);
    }

    @Test
    public void generateExponentsTest() {
        FastExponentiation.generateExponentsOfNumberNUptoK(9812, 7915, 12091);
    }
}