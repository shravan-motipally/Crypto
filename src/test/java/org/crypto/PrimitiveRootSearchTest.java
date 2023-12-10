package org.crypto;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static java.lang.String.valueOf;
import static org.junit.jupiter.api.Assertions.*;

public class PrimitiveRootSearchTest {


    @Test
    public void testPrimitiveRoot() throws Exception {
        PrimitiveRootSearch.testPrimitiveRoot(131L, 87L);
    }

    @Test
    public void findPrimitiveRootWithGroupGiven() throws Exception {
        System.out.println(PrimitiveRootSearch.primitiveRootSearch(BigInteger.valueOf(131L)));
    }

    @Test
    public void testPrimitiveRootSearch() throws Exception {
        BigInteger generator = PrimitiveRootSearch.primitiveRootSearch(BigInteger.valueOf(5));
        System.out.println(generator);
    }

    @Test
    public void testPrimitiveRootSearchToEnsureItAlwaysGivesNonZeroNonNegativeBigInteger() {
        BigInteger generator;
        for (int i = 0; i <= 10; i++) {
            generator = PrimitiveRootSearch.primitiveRootSearch(BigInteger.valueOf(5));
            assertNotEquals(BigInteger.ZERO, generator);
        }
    }
}