package org.crypto;

import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.valueOf;
import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

class EuclideanTest {

    @Test
    public void gcdTest() {
        assertEquals(Euclidean.findGcdUsingEuclidean(12, 8).getLeft(), 4);
        assertEquals(Euclidean.findGcdUsingEuclidean(53, 37).getLeft(), 1);
    }

    @Test
    public void gcdTestUsingLongs() {
        assertEquals(Euclidean.findGcdUsingEuclidean(12L, 37L).getLeft(), 1);
    }


    @Test
    public void expandedGcdTest() {
        Pair<Integer, Integer> xyPair = Euclidean.findXYForExtendedEuclidean(102313, 103927);
        assertEquals(xyPair.getLeft(), 39858);
        assertEquals(xyPair.getRight(), -39239);
    }

    @Test
    public void zeroTest() {
        Pair<BigInteger, BigInteger> xyPair = Euclidean.findXYForExtendedEuclidean(ZERO, valueOf(103927));
    }

    @Test
    public void expandedGcdTestForTest() {
        Pair<Integer, Integer> xyPair = Euclidean.findXYForExtendedEuclidean(3, 11872);
    }

    @Test
    public void expandedGcdTestWithBigIntegers() {
        Pair<BigInteger, BigInteger> xyPair = Euclidean.findXYForExtendedEuclidean(valueOf(102313), valueOf(103927));
        assertEquals(xyPair.getLeft(), valueOf(39858));
        assertEquals(xyPair.getRight(), valueOf(-39239));
    }

    @Test
    public void getDecryptionExponentIfPhiAndExponentKnown() {
        Pair<BigInteger, BigInteger> xyPair = Euclidean.findXYForExtendedEuclidean(valueOf(3), valueOf(15688));
        assertTrue(xyPair.getLeft().equals(valueOf(-5229)) || xyPair.getLeft().equals(valueOf(15688 - 5229)));
    }
}