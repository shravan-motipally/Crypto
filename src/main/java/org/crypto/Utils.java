package org.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class Utils {
    public static final String DOUBLE_TAB = "\t\t";

    public static BigInteger randomBigIntegerWithin(BigInteger max) {
        if (max.compareTo(ONE) < 0) {
            throw new RuntimeException("Cannot generate random non zero big integer less than given max value.");
        } else if (max.equals(ONE)) {
            return ONE;
        }
        SecureRandom random = new SecureRandom();
        BigInteger test;
        do {
            test = new BigInteger(max.bitLength(), random);
        } while (test.compareTo(max) >= 0 || test.equals(ZERO));
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
