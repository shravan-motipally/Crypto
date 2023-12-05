package org.crypto;

import java.math.BigInteger;

import static org.crypto.Utils.DOUBLE_TAB;

public class FastExponentiation {

    public static final String FAST_EXPONENTIATION_TEMPLATE = "%d" + DOUBLE_TAB + "%d" + DOUBLE_TAB + "%d\n";
    public static long fastExponentiation(long x, long e, long mod) {
        long y = 1;
        System.out.printf("x" + DOUBLE_TAB + "e" + DOUBLE_TAB + "Y\n");
        System.out.printf(FAST_EXPONENTIATION_TEMPLATE, x, e, y);
        while (e != 0) {
            if (e%2 == 0) {
                x = (x*x) % mod;
                e = e/2;
            } else {
                y = (x*y) % mod;
                e = e - 1;
            }
            System.out.printf(FAST_EXPONENTIATION_TEMPLATE, x, e, y);
        }
        return y;
    }

    public static BigInteger fastExponentiation(BigInteger x, BigInteger e, BigInteger mod) {
        BigInteger y = BigInteger.valueOf(1);
        // System.out.printf("x" + DOUBLE_TAB + "e" + DOUBLE_TAB + "Y\n");
        // System.out.printf(FAST_EXPONENTIATION_TEMPLATE, x, e, y);
        while (!e.equals(BigInteger.valueOf(0))) {
            if (e.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                x = (x.multiply(x)).mod(mod);
                e = e.divide(BigInteger.TWO);
            } else {
                y = (x.multiply(y)).mod(mod);
                e = e.subtract(BigInteger.ONE);
            }
            // System.out.printf(FAST_EXPONENTIATION_TEMPLATE, x, e, y);
        }
        return y;
    }

    public static void generateExponentsOfNumberNUptoK(long n, int k, long mod) {
        for (int i = 1; i <= k; i++) {
            System.out.printf("%d ^ %d = %d\n", n, i, (long) (Math.pow(n, i) % mod));
        }
        System.out.println();
    }
}
