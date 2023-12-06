package org.crypto;

import model.RSAPair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static org.crypto.Euclidean.findGcdUsingEuclidean;
import static org.crypto.FastExponentiation.fastExponentiation;
import static org.crypto.Primes.isPrimeMillerRabin;
import static org.crypto.Primes.phi;
import static org.crypto.RandomGenerator.bbgGenerator;

public class RSA {

    private static Logger logger = LoggerFactory.getLogger(RSA.class);

    public static BigInteger encrypt(BigInteger plaintext, RSAPair publicKey) {
        logger.info("Creating cipher text for {} with publicKey {}", plaintext, publicKey);
        return fastExponentiation(plaintext, publicKey.getExponent(), publicKey.getModulus());
    }

    public static BigInteger decrypt(BigInteger ciphertext, RSAPair privateKey) {
        logger.info("Creating plain text for {} with privateKey {}", ciphertext, privateKey);
        return fastExponentiation(ciphertext, privateKey.getExponent(), privateKey.getModulus());
    }

    public static Pair<BigInteger, BigInteger> generatePAndQ() {
        BigInteger p = Primes.generateLargePrimeWithNBits(1024);
        while (!p.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
            p = Primes.generateLargePrimeWithNBits(1024);
        }
        System.out.println("Chosen p value: " + p);
        BigInteger q = Primes.generateLargePrimeWithNBits(1024);
        while (!q.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
            q = Primes.generateLargePrimeWithNBits(1024);
        }
        return Pair.of(p, q);
    }

    public static Pair<BigInteger, BigInteger> generatePAndQ(int maxBits) {
        BigInteger p = Primes.generateLargePrimeWithNBits(maxBits);
        while (!p.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
            p = Primes.generateLargePrimeWithNBits(maxBits);
        }
        System.out.println("Chosen p value: " + p);
        BigInteger q = Primes.generateLargePrimeWithNBits(maxBits);
        while (!q.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
            q = Primes.generateLargePrimeWithNBits(maxBits);
        }
        return Pair.of(p, q);
    }

    public static BigInteger generateExponent(Pair<BigInteger, BigInteger> pair) {
//        if (!isPrimeMillerRabin(pair.getLeft(), 5) || !isPrimeMillerRabin(pair.getRight(), 5)) {
//            throw new RuntimeException("One of both prime numbers passed in were not prime.");
//        }
        BigInteger phi = pair.getLeft().subtract(ONE).multiply(pair.getRight().subtract(ONE)); // we're assuming the pair is prime.

        BigInteger exponent;
        do {
            // if you want speed use Utils.randomBigIntegerWithinrandomBigIntegerWithin(phi)
            exponent = Utils.randomBigIntegerWithin(phi);//bbgGenerator(phi.bitLength());
            // we keep going until we get a number that is relatively prime to phi(n).
        } while(!ONE.equals(findGcdUsingEuclidean(exponent, phi).getLeft()));
        return exponent;
    }

    public static Pair<RSAPair, RSAPair> generatePublicAndPrivateKey() {
        Pair<BigInteger, BigInteger> pq = generatePAndQ();
        System.out.println("generated p and q");
        BigInteger exponent = generateExponent(pq);
        System.out.println("Generated exponent");
        return Pair.of(new RSAPair(pq.getLeft().multiply(pq.getRight()), exponent), null);
    }

    public static Pair<RSAPair, RSAPair> generatePublicAndPrivateKey(int maxBits) {
        Pair<BigInteger, BigInteger> pq = generatePAndQ(maxBits);
        System.out.println("generated p and q");
        BigInteger exponent = generateExponent(pq);
        System.out.println("Generated exponent");
        return Pair.of(new RSAPair(pq.getLeft().multiply(pq.getRight()), exponent), null);
    }
}
