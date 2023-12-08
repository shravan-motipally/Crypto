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




}