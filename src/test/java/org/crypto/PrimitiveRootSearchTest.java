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
}