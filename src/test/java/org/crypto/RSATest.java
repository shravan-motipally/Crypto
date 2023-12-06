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
        BigInteger decryptedMsg = decrypt(encryptedMsg, pair.getRight());
        assertEquals(msg, decryptedMsg);
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