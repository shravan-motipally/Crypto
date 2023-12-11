package org.crypto;

import model.RSAPair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static java.math.BigInteger.*;
import static org.crypto.RSA.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RSATest {

    @Test
    public void generateBobsPublicKey() {
        Pair<RSAPair, RSAPair> bobsKeys = generatePublicAndPrivateKey(24);
        System.out.println("Bob's public key: " + bobsKeys.getLeft());
        System.out.println("Bob's private key: " + bobsKeys.getRight());
    }

    @Test
    public void encryptionTestRSA() {
        BigInteger msg = Utils.randomBigIntegerWithin(TWO.pow(24));
        System.out.println("Alice trying to send the following message (in unecrypted form): " + msg + "");
        Pair<RSAPair, RSAPair> bobsPair = generatePublicAndPrivateKey(24);
        System.out.println("Bob's public and private keys: ");
        System.out.println("Public: " + bobsPair.getLeft());
        System.out.println("Private: " + bobsPair.getRight());
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
    public void decryptMessageTest() {
        BigInteger aliceEncryptedMsg = new BigInteger("35654044114285");
        RSAPair bobsPrivKey = new RSAPair(new BigInteger("7320868104187"), new BigInteger("94762930171321"));
        BigInteger decryptedMsg = RSA.decrypt(aliceEncryptedMsg, bobsPrivKey);
        System.out.println(decryptedMsg);
    }

    @Test
    public void decryptionTestRSA() {
        BigInteger p = new BigInteger("15918611");
        BigInteger q = new BigInteger("11733763");
        BigInteger n = p.multiply(q);
        BigInteger message = new BigInteger("4982934");
        BigInteger aliceEncryptedMsg = new BigInteger("142571086419239");
        RSAPair bobsPubKey = new RSAPair(new BigInteger("61372794578543"), new BigInteger("186785208763193"));
        RSAPair bobsPrivKey = new RSAPair(new BigInteger("122018261679707"), new BigInteger("186785208763193"));
        assertEquals(ONE, bobsPubKey.getExponent().multiply(bobsPrivKey.getExponent()).mod(p.subtract(ONE).multiply(q.subtract(ONE))));
        BigInteger decryptedMsg = RSA.decrypt(aliceEncryptedMsg, bobsPrivKey);

        System.out.println(decryptedMsg);
        assertEquals(message, decryptedMsg);
    }

    @Test
    public void eavesdroppingTestDuringPairing() {
        BigInteger msg = new BigInteger("173132106911584");
        RSAPair eavesdroppedRSAPair = new RSAPair(new BigInteger("155212581005987"), new BigInteger("199472392197209"));
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