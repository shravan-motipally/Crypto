package org.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;

import static java.math.BigInteger.ONE;

public class Utils {
    public static final String DOUBLE_TAB = "\t\t";

    public static BigInteger randomBigIntegerWithin(BigInteger max) {
        SecureRandom random = new SecureRandom();
        BigInteger test;
        do {
            test = new BigInteger(max.bitLength(), random);
        } while (test.compareTo(max) >= 0);
        return test;
    }

    public static BigInteger randomBigIntegerWithin(BigInteger min, BigInteger max) {
        SecureRandom random = new SecureRandom();
        BigInteger test;
        do {
            test = new BigInteger(max.bitLength(), random);
        } while (test.compareTo(min) < 0 || test.compareTo(max) >= 0);
        return test;
    }

}
