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
    public void encipherTest() {
        BigInteger msg = Utils.randomBigIntegerWithin(TWO.pow(24));
        Pair<RSAPair, RSAPair> pair = generatePublicAndPrivateKey(24);
        assertEquals(pair.getLeft().getModulus(), pair.getRight().getModulus());
        BigInteger encryptedMsg = encrypt(msg, pair.getLeft());
        System.out.println();
//        BigInteger decryptedMsg = decrypt(encryptedMsg, pair.getRight());
//        assertEquals(msg, decryptedMsg);
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
    public void eavesdroppingTestDuringPairing() {
        BigInteger msg = new BigInteger("35115497060646");//Utils.randomBigIntegerWithin(TWO.pow(24));
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

        BigInteger decryptedMsg = FastExponentiation.fastExponentiation(encryptedMsg, decryptionKey, pubKey.getModulus());
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
    public void generatePublicKey() {
        Pair<RSAPair, RSAPair> pair = generatePublicAndPrivateKey();
        System.out.println(pair.getLeft());
    }

    @Test
    public void generatePublicKeyWithMaximumBits() {
        Pair<RSAPair, RSAPair> pair = generatePublicAndPrivateKey(24);
        System.out.println(pair.getLeft());
    }

}