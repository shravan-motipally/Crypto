package org.crypto;

import model.ElGamalPair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static java.math.BigInteger.*;
import static org.crypto.ElGamal.generateCommonGroup;
import static org.crypto.ElGamal.getJointKey;
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
        Pair<ElGamalPair, BigInteger> ourDetails = ElGamal.generatePublicKeyAndSecret(group);

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
    public void testElGamalWithTeamAsAlice() {
        BigInteger group = new BigInteger("10901741");
        BigInteger generator = new BigInteger("7200621");
        BigInteger myEncryptionKey = new BigInteger("4855820");
        BigInteger mySecret = new BigInteger("8316155");
        BigInteger theirKey = new BigInteger("7106903");
        assertTrue(isPrimeMillerRabin(group, 5));

        BigInteger message = new BigInteger("9458119"); // as group is prime

        Pair<ElGamalPair, BigInteger> ourDetails = Pair.of(new ElGamalPair(group, generator, myEncryptionKey), mySecret);
        Pair<ElGamalPair, BigInteger> otherDetails = Pair.of(new ElGamalPair(group, generator, theirKey), null);

        BigInteger encryptedMessage = ElGamal.encrypt(message, group, ourDetails.getRight(), otherDetails.getLeft());

        //Share the following with the team.
        System.out.println("Encrypted message: " + encryptedMessage);
    }


    @Test
    public void testElGamalWithTeamAsBob() {
        BigInteger group = new BigInteger("16300051");
        BigInteger generator = new BigInteger("6353629");
        BigInteger aliceEncryptionKey = new BigInteger("1183985");
        BigInteger mySecret = new BigInteger("4309990");
        BigInteger bobKey = new BigInteger("1378805");
        assertTrue(isPrimeMillerRabin(group, 5));

        BigInteger message = new BigInteger("11335333");
        assert(message.compareTo(group) < 0);

        Pair<ElGamalPair, BigInteger> aliceDetails = Pair.of(new ElGamalPair(group, generator, aliceEncryptionKey), null);
        Pair<ElGamalPair, BigInteger> otherDetails = Pair.of(new ElGamalPair(group, generator, bobKey), mySecret);

        BigInteger encryptedMessage = new BigInteger("8200878");
        assertEquals(message, ElGamal.decrypt(encryptedMessage, group, otherDetails.getRight(), aliceDetails.getLeft()));
    }

    @Test
    public void testElGamalAsEve() {
        BigInteger group = valueOf(16396147L);
        BigInteger generator = new BigInteger("2");
        BigInteger encryptedMessage = new BigInteger("14521569");
        ElGamalPair bobEncKey = new ElGamalPair(group, generator, new BigInteger("15682381"));
        ElGamalPair aliceEncKey = new ElGamalPair(group, generator, new BigInteger("7197288"));

        BigInteger message = new BigInteger("");

        assertEquals(message, ElGamal.eavesdrop(encryptedMessage, aliceEncKey, bobEncKey));
    }

}