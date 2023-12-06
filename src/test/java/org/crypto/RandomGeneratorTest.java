package org.crypto;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static java.math.BigInteger.TWO;
import static org.junit.jupiter.api.Assertions.*;

public class RandomGeneratorTest {

    @Test
    public void testBbgGeneration() {
        BigInteger integer = RandomGenerator.bbgGenerator(24);
        assertTrue(integer.compareTo(TWO.pow(24)) < 0);
        System.out.println("integer generated: " + integer);
    }

}