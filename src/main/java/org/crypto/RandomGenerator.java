package org.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;
import static org.crypto.Utils.randomBigIntegerWithin;

public class RandomGenerator {

    public static BigInteger bbgGenerator(int maxBits) {
        BigInteger p = Primes.generateLargePrimeWithNBits(maxBits);
        while (!p.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
            p = Primes.generateLargePrimeWithNBits(maxBits);
        }
        System.out.println("Chosen p value: " + p);
        BigInteger q = Primes.generateLargePrimeWithNBits(maxBits);
        while (!q.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
            q = Primes.generateLargePrimeWithNBits(maxBits);
        }
        System.out.println("Chosen q value: " + q);
        BigInteger n = p.multiply(q);
        System.out.printf("%s = %s * %s\n", n, p, q);
        BigInteger seed = randomBigIntegerWithin(n);
        StringBuilder randValueStr = new StringBuilder();
        for (int i = 0; i < maxBits; i++) {
            String value = seed.mod(TWO).equals(ONE) ? "1" : "0";
            System.out.printf("Seed: %s, seed mod 2: %s\n", seed, value);
            seed = (seed.multiply(seed)).mod(n);
            randValueStr.insert(0, value);
            System.out.printf("CurrentRandValueStr: %s\n", randValueStr);
        }
        return new BigInteger(randValueStr.toString(), 2);
    }
}
