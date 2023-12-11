package org.crypto;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigInteger.*;
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

    public static BigInteger NaorReingoldGenerator(int maxBits) {
        BigInteger p = Primes.generateLargePrimeWithNBits(maxBits);
        System.out.println("Chosen p value: " + p);
        BigInteger q = Primes.generateLargePrimeWithNBits(maxBits);
        System.out.println("Chosen q value: " + q);
        BigInteger n = p.multiply(q);
        Map<Integer, Pair<BigInteger, BigInteger>> a = new HashMap<>();
        for (int i = 0; i < maxBits; i++) {
            BigInteger randomKey = randomBigIntegerWithin(n); // a_i_0
            BigInteger randomVal = randomBigIntegerWithin(n); //a_i_1
            a.put(i, Pair.of(randomKey, randomVal));
        }

        // Choosing g in Z_n - just choose something other than p and q.
        BigInteger randomRelativePrime  = randomBigIntegerWithin(n);
        while (randomRelativePrime.equals(p) || randomRelativePrime.equals(q)) {
            randomRelativePrime  = randomBigIntegerWithin(n);
        }

        BigInteger g = randomRelativePrime.multiply(randomRelativePrime).mod(n);

        char[] yetAnotherRandomValue = randomBigIntegerWithin(n).toString(2).toCharArray();
        BigInteger builtInteger = ZERO;
        for (int i = 0; i < maxBits; i++) {
            if (i >= maxBits - yetAnotherRandomValue.length) {
                builtInteger = (yetAnotherRandomValue[i] == '0' ? a.get(i).getLeft() : a.get(i).getRight()).add(builtInteger);
            } else {
                builtInteger = builtInteger;
            }
        }

        String betaValue = beta(maxBits, builtInteger);
        String r = getRandom2nBitString(maxBits);
        return null;
    }

    public static String beta(int maxBits, BigInteger number) {
        String zeroes = "";
        int bitsToFill = maxBits - number.bitLength();
        for (int i = 0; i < maxBits + bitsToFill; i++) {
            zeroes += "0";
        }
        return zeroes + number.toString(2);
    }

    public static String getRandom2nBitString(int n) {
        StringBuilder val = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 2*n; i++) {
            val.append(random.nextBoolean() ? "0" : "1");
        }
        return val.toString();
    }

    public static BigInteger dotProduct(String u, String v) {
        return null;
    }
}
