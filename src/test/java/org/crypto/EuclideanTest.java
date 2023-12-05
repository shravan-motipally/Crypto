package org.crypto;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
class EuclideanTest {

    @Test
    public void gcdTest() {
        assertEquals(Euclidean.findGcdUsingEuclidean(12, 8).getLeft(), 4);
        assertEquals(Euclidean.findGcdUsingEuclidean(53, 37).getLeft(), 1);
//        System.out.println(Euclidean.findGcdUsingEuclidean(0, 5));
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
    public void expandedGcdTestForTest() {
        Pair<Integer, Integer> xyPair = Euclidean.findXYForExtendedEuclidean(3, 11872);
    }
}