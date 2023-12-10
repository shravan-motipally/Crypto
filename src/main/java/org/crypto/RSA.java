package org.crypto;

import model.RSAPair;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.util.Map;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static org.crypto.Euclidean.findGcdUsingEuclidean;
import static org.crypto.Factorization.findAllFactorsUsingRhoFactorization;
import static org.crypto.FastExponentiation.fastExponentiation;

public class RSA {

    public static BigInteger encrypt(BigInteger plaintext, RSAPair publicKey) {
        System.out.printf("Creating cipher text for %s with publicKey %s\n", plaintext, publicKey);
        return fastExponentiation(plaintext, publicKey.getExponent(), publicKey.getModulus());
    }

    public static BigInteger decrypt(BigInteger ciphertext, RSAPair privateKey) {
        System.out.printf("Creating plain text for %s with privateKey %s\n", ciphertext, privateKey);
        return fastExponentiation(ciphertext, privateKey.getExponent(), privateKey.getModulus());
    }

    public static BigInteger eavesdrop(BigInteger ciphertext, RSAPair publicKey) {
        Map<BigInteger, Integer> factorization = findAllFactorsUsingRhoFactorization(publicKey.getModulus());
        assert(factorization.size() == 2);
        BigInteger[] factors = factorization.keySet().toArray(BigInteger[]::new);
        BigInteger phi = factors[0].subtract(ONE).multiply(factors[1].subtract(ONE));
        Pair<BigInteger, BigInteger> coeff = Euclidean.findXYForExtendedEuclidean(publicKey.getExponent(), phi);

        BigInteger decryptionCoefficient = coeff.getLeft();

        if (decryptionCoefficient.compareTo(ZERO) < 0) {
            decryptionCoefficient = decryptionCoefficient.add(phi);
        }
        return fastExponentiation(ciphertext, decryptionCoefficient, publicKey.getModulus());
    }

    public static Pair<BigInteger, BigInteger> generatePAndQ(int maxBits) {
        BigInteger p = Primes.generateLargePrimeWithNBits(maxBits);
        while (!p.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
            p = Primes.generateLargePrimeWithNBits(maxBits);
        }
        BigInteger q = Primes.generateLargePrimeWithNBits(maxBits);
        while (!q.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
            q = Primes.generateLargePrimeWithNBits(maxBits);
        }
        return Pair.of(p, q);
    }

    public static Pair<BigInteger, BigInteger> generateEncryptionAndDecryptionExponents(Pair<BigInteger, BigInteger> pair) {
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

        Pair<BigInteger, BigInteger> coeff = Euclidean.findXYForExtendedEuclidean(exponent, phi);

        BigInteger leftCoeff = coeff.getLeft();

        if (leftCoeff.compareTo(ZERO) < 0) {
            leftCoeff = leftCoeff.add(phi);
        }

        return Pair.of(exponent, leftCoeff);
    }

    public static Pair<RSAPair, RSAPair> generatePublicAndPrivateKey(int maxBits) {
        Pair<BigInteger, BigInteger> pq = generatePAndQ(maxBits);
        BigInteger n = pq.getLeft().multiply(pq.getRight());
        System.out.println("Bob generated p and q, p = " + pq.getLeft() + ", q = " + pq.getRight());
        Pair<BigInteger, BigInteger> encryptionDecryptionExponents = generateEncryptionAndDecryptionExponents(pq);
        BigInteger encryptionExponent = encryptionDecryptionExponents.getLeft();
        System.out.println("Generated encryptionExponent: " + encryptionExponent);
        RSAPair left = new RSAPair(encryptionExponent, n);

        BigInteger decryptionExponent = encryptionDecryptionExponents.getRight();
        System.out.println("Generated decryptionExponent: " + decryptionExponent);
        RSAPair right = new RSAPair(decryptionExponent, n);

        return Pair.of(left, right);
    }

//    public static RSAPair generatePublicKey
}
