package org.crypto;

import model.RSAPair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static java.math.BigInteger.*;

import static org.crypto.RSA.*;
import static org.junit.jupiter.api.Assertions.*;

public class RSATest {

    @Test
    public void encryptionTestRSA() {
        BigInteger msg = Utils.randomBigIntegerWithin(TWO.pow(24));
        System.out.println("Alice trying to send the following message (in unecrypted form): " + msg + "");
        Pair<RSAPair, RSAPair> bobsPair = generatePublicAndPrivateKey(24);
        assertEquals(bobsPair.getLeft().getModulus(), bobsPair.getRight().getModulus());
        BigInteger encryptedMsg = encrypt(msg, bobsPair.getLeft());
        System.out.println("Alice's encrypted message: " + encryptedMsg);
        assertEquals(FastExponentiation.fastExponentiation(msg, bobsPair.getLeft().getExponent(), bobsPair.getLeft().getModulus()),
                encryptedMsg);
    }

    @Test
    public void eavesdroppingTest() {
        BigInteger msg = Utils.randomBigIntegerWithin(TWO.pow(24));
        Pair<RSAPair, RSAPair> pair = generatePublicAndPrivateKey(24);
        assertEquals(pair.getLeft().getModulus(), pair.getRight().getModulus());
        BigInteger encryptedMsg = encrypt(msg, pair.getLeft());
        BigInteger eavesdroppedMsg = eavesdrop(encryptedMsg, pair.getLeft());
        assertEquals(msg, eavesdroppedMsg);
    }

    @Test
    public void decryptionTestRSA() {
        BigInteger p = new BigInteger("9107159");
        BigInteger q = new BigInteger("15289943");
        BigInteger message = new BigInteger("3333");
        BigInteger aliceEncryptedMsg = new BigInteger("38122060084554");
        RSAPair bobsPubKey = new RSAPair(new BigInteger("13647839"), new BigInteger("139247942001937"));
        RSAPair bobsPrivKey = new RSAPair(new BigInteger("87885123486543"), new BigInteger("139247942001937"));
        assertEquals(ONE, bobsPubKey.getExponent().multiply(bobsPrivKey.getExponent()).mod(p.subtract(ONE).multiply(q.subtract(ONE))));
        BigInteger decryptedMsg = RSA.decrypt(aliceEncryptedMsg, bobsPrivKey);
        assertEquals(message, decryptedMsg);
    }

    @Test
    public void eavesdroppingTestDuringPairing() {
        BigInteger msg = new BigInteger("35115497060646");
        RSAPair eavesdroppedRSAPair = new RSAPair(new BigInteger("13647839"), new BigInteger("139247942001937"));
        BigInteger eavesdroppedMsg = eavesdrop(msg, eavesdroppedRSAPair);
        System.out.println(eavesdroppedMsg);
    }

    @Test
    public void decryptionTestDuringPairing() {
        BigInteger encryptedMsg = new BigInteger("38122060084554");
        RSAPair pubKey = new RSAPair(new BigInteger("13647839"), new BigInteger("139247942001937"));
        BigInteger phi = Primes.phi(pubKey.getModulus(), Factorization.findAllFactorsUsingRhoFactorization(pubKey.getModulus()));
        Pair<BigInteger, BigInteger> coeff = Euclidean.findXYForExtendedEuclidean(pubKey.getExponent(), phi);

        BigInteger decryptionKey = coeff.getLeft();

        if (decryptionKey.compareTo(ZERO) < 0) {
            decryptionKey = decryptionKey.add(phi);
        }

        BigInteger decryptedMsg = RSA.decrypt(encryptedMsg, new RSAPair(decryptionKey, pubKey.getModulus())); //FastExponentiation.fastExponentiation(encryptedMsg, decryptionKey, pubKey.getModulus());
        System.out.println(decryptedMsg);
    }

    @Test
    public void ensureEncryptionDecryptionExponentsMultiplyTo1ModPhi() {
        Pair<BigInteger, BigInteger> pq = generatePAndQ(24);
        Pair<BigInteger, BigInteger> pair = generateEncryptionAndDecryptionExponents(pq);
        BigInteger phi = pq.getLeft().subtract(ONE).multiply(pq.getRight().subtract(ONE));
        assertEquals(pair.getLeft().multiply(pair.getRight()).mod(phi), ONE);
    }

    @Test
    public void generatePublicKeyWithMaximumBits() {
        Pair<RSAPair, RSAPair> pair = generatePublicAndPrivateKey(24);
        System.out.println(pair.getLeft());
    }

}