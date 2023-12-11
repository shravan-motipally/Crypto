package org.crypto;

import model.ElGamalPair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.HashMap;

import static java.math.BigInteger.*;
import static org.crypto.ElGamal.*;
import static org.crypto.FastExponentiation.fastExponentiation;
import static org.crypto.Primes.isPrimeMillerRabin;
import static org.junit.jupiter.api.Assertions.*;

public class ElGamalTest {

    @Test
    public void ensureGroupGeneratedIsAPrimeNumber() {
        BigInteger groupGenerated = generateCommonGroup(24);
        assertTrue(isPrimeMillerRabin(groupGenerated, 5));
    }

    @Test
    public void ensureGroupGenerationValidatesInput() {
        assertThrows(RuntimeException.class, () -> {
            generateCommonGroup(-1);
        });
        assertThrows(RuntimeException.class, () -> {
            generateCommonGroup(0);
        });
    }

    @Test
    public void ensurePublicKeyGeneratedCorrectly() {
        BigInteger group = Primes.generateLargePrimeWithNBits(24);
        Pair<ElGamalPair, BigInteger> keyAndSecret = generatePublicKeyAndSecret(group);
        BigInteger secret = keyAndSecret.getRight();
        ElGamalPair pubKey = keyAndSecret.getLeft();
        assertEquals(pubKey.getGenerator().modPow(secret, group), pubKey.getEncryptionKey());
        keyAndSecret = generatePublicKeyAndSecret(group);
        if (secret.equals(keyAndSecret.getRight())) { // not asserting here as there is very little chance of generating same, but is non zero.
            System.err.println("Same secret used for public key generation.");
        }
    }

    @Test
    public void ensurePublicKeyGeneratorIsGeneratedCorrectly() {
        BigInteger group = Primes.generateLargePrimeWithNBits(10);
        Pair<ElGamalPair, BigInteger> keyAndSecret = generatePublicKeyAndSecret(group);
        BigInteger generator = keyAndSecret.getLeft().getGenerator();
        HashMap<BigInteger, Boolean> elements = new HashMap<>();

        for (BigInteger i = ZERO; i.compareTo(group.subtract(ONE)) < 0; i = i.add(ONE)) {
            BigInteger val = generator.modPow(i, group);
            elements.put(val, true);
        }

        for (BigInteger i = ONE; i.compareTo(group.subtract(ONE)) <= 0; i = i.add(ONE)) {
            assertTrue(elements.get(i));
        }
    }

    @Test
    public void ensureInverseMethodWorksAsIntended() {
        BigInteger prime = Primes.generateLargePrimeWithNBits(24);
        BigInteger randomNumber = Utils.randomBigIntegerWithin(prime);
        BigInteger generator = PrimitiveRootSearch.primitiveRootSearch(prime);
        BigInteger sampleJointKey = fastExponentiation(generator, randomNumber, prime);
        BigInteger inverse = fastExponentiation(sampleJointKey, prime.subtract(TWO), prime);
        assertEquals(ONE, sampleJointKey.multiply(inverse).mod(prime));
    }

    @Test
    public void testElGamalWithTeamMember() {
        BigInteger group = generateCommonGroup(24);
        assertTrue(isPrimeMillerRabin(group, 5));
        Pair<ElGamalPair, BigInteger> ourDetails = generatePublicKeyAndSecret(group);

        // here we ask them for their public key only.
        Pair<ElGamalPair, BigInteger> otherDetails = ElGamal.generatePublicKeyAndSecretUsingKnownGenerator(group, ourDetails.getLeft().getGenerator());
//        otherDetails.setValue(ZERO); // This just means that we don't care about their secret.
        // ^ uncomment during exercise.

        // test joint key for alice and bob.
        BigInteger myJointKey = getJointKey(group, ourDetails.getRight(), otherDetails.getLeft());
        BigInteger theirJointKey = getJointKey(group, otherDetails.getRight(), ourDetails.getLeft());

        // basic test of equality
        assertEquals(myJointKey, theirJointKey);

        // Set the message here.
        BigInteger message = Utils.randomBigIntegerWithin(group); // as group is prime
        System.out.println(message);

        // Send the message over.
        BigInteger encryptedMessage = ElGamal.encrypt(message, group, ourDetails.getRight(), otherDetails.getLeft());
        System.out.println(encryptedMessage);


        // As the other party, trying to decrypt the message.
        assertEquals(message, ElGamal.decrypt(encryptedMessage, group, otherDetails.getRight(), ourDetails.getLeft()));
    }

    @Test
    public void generateGroupAndGenerator() {
        BigInteger group = generateCommonGroup(24);
        assertTrue(isPrimeMillerRabin(group, 5));
        Pair<ElGamalPair, BigInteger> ourDetails = generatePublicKeyAndSecret(group);
        System.out.println(ourDetails.getLeft());
        System.out.println("encryption secret (e): " + ourDetails.getRight());
    }

    @Test
    public void generateBobsEncryptionKey() {
        BigInteger group = new BigInteger("11171861");
        BigInteger generator = new BigInteger("772296");
        Pair<ElGamalPair, BigInteger> bobsDetails = generatePublicKeyAndSecretUsingKnownGenerator(group, generator);
        System.out.println(bobsDetails.getLeft());
        System.out.println("bobs encryption secret (e): " + bobsDetails.getRight());
    }

    @Test
    public void testElGamalAsAlice() {
        BigInteger group = new BigInteger("10120921");
        BigInteger generator = new BigInteger("7461015");
        BigInteger aliceEncryptionKey = new BigInteger("6092743");
        BigInteger aliceSecret = new BigInteger("1015823");
        BigInteger bobEncryptionKey = new BigInteger("7868224");
        assertTrue(isPrimeMillerRabin(group, 10));

        BigInteger message = new BigInteger("9458119"); // as group is prime
        assertTrue(message.compareTo(group) < 0);

        /**
         * ElGamalPair (group, generator, encryption key = generator ^ secret mod group)
         */
        Pair<ElGamalPair, BigInteger> ourDetails = Pair.of(new ElGamalPair(group, generator, aliceEncryptionKey), aliceSecret);
        Pair<ElGamalPair, BigInteger> otherDetails = Pair.of(new ElGamalPair(group, generator, bobEncryptionKey), null);

        BigInteger encryptedMessage = ElGamal.encrypt(message, group, ourDetails.getRight(), otherDetails.getLeft());

        //Share the following with the team.
        System.out.println("Encrypted message: " + encryptedMessage);
        assertEquals(message.multiply(
                FastExponentiation.fastExponentiation(otherDetails.getLeft().getEncryptionKey(), ourDetails.getRight(), group))
                .mod(group), encryptedMessage);
    }


    @Test
    public void testElGamalAsBob() {
        BigInteger group = new BigInteger("16300051");
        BigInteger generator = new BigInteger("6353629");
        BigInteger aliceEncryptionKey = new BigInteger("1183985");
        BigInteger bobSecret = new BigInteger("4309990");
        BigInteger bobKey = new BigInteger("1378805");
        assertTrue(isPrimeMillerRabin(group, 5));

        BigInteger message = new BigInteger("11335333");
        assert(message.compareTo(group) < 0);

        /**
         * ElGamalPair (group, generator, encryption key = generator ^ secret mod group)
         */
        Pair<ElGamalPair, BigInteger> aliceDetails = Pair.of(new ElGamalPair(group, generator, aliceEncryptionKey), null);
        Pair<ElGamalPair, BigInteger> otherDetails = Pair.of(new ElGamalPair(group, generator, bobKey), bobSecret);

        BigInteger encryptedMessage = new BigInteger("8200878");
        BigInteger decryptedMessage = ElGamal.decrypt(encryptedMessage, group, otherDetails.getRight(), aliceDetails.getLeft());

        System.out.println("Decrypted message: " + decryptedMessage);
        assertEquals(decryptedMessage, FastExponentiation.fastExponentiation(
                FastExponentiation.fastExponentiation(aliceDetails.getLeft().getEncryptionKey(), otherDetails.getRight(), group),
                group.subtract(TWO), group).multiply(encryptedMessage).mod(group));
        assertEquals(message, decryptedMessage);
    }

    @Test
    public void testElGamalAsEve() {
        BigInteger group = valueOf(16396147L);
        BigInteger generator = new BigInteger("2");
        BigInteger encryptedMessage = new BigInteger("14521569");
        ElGamalPair bobEncKey = new ElGamalPair(group, generator, new BigInteger("15682381"));
        ElGamalPair aliceEncKey = new ElGamalPair(group, generator, new BigInteger("7197288"));

        BigInteger message = new BigInteger("1314");
        BigInteger eavesdroppedMessage = ElGamal.eavesdrop(encryptedMessage, aliceEncKey, bobEncKey);

        BigInteger recoveredAliceSecret = LittleStepGiantStep.findDiscreteLog_B_Of_A_InZ_P(generator, aliceEncKey.getEncryptionKey(), group);
        BigInteger recoveredBobSecret = LittleStepGiantStep.findDiscreteLog_B_Of_A_InZ_P(generator, bobEncKey.getEncryptionKey(), group);
        BigInteger jointKey = fastExponentiation(generator, recoveredAliceSecret.multiply(recoveredBobSecret), group);
        BigInteger inverse = fastExponentiation(jointKey, group.subtract(TWO), group);

        assertEquals(eavesdroppedMessage, encryptedMessage.multiply(inverse).mod(group));
        assertEquals(message, eavesdroppedMessage);
    }

}