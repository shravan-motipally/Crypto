package org.crypto;

import model.ElGamalPair;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;
import static org.crypto.FastExponentiation.fastExponentiation;
import static org.crypto.PrimitiveRootSearch.primitiveRootSearch;

public class ElGamal {

    public static BigInteger generateCommonGroup(int maxBits) {
        return Primes.generateLargePrimeWithNBits(maxBits);
    }

    public static Pair<ElGamalPair, BigInteger> generatePublicKeyAndSecret(BigInteger group) {
        BigInteger generator = primitiveRootSearch(group);
        BigInteger r = Utils.randomBigIntegerWithin(group);
        ElGamalPair pubKey = new ElGamalPair(group, generator, fastExponentiation(generator, r, group));
        return Pair.of(pubKey, r);
    }

    public static Pair<ElGamalPair, BigInteger> generatePublicKeyAndSecretUsingKnownGenerator(BigInteger group, BigInteger generator) {
        BigInteger r = Utils.randomBigIntegerWithin(group);
        ElGamalPair pubKey = new ElGamalPair(group, generator, fastExponentiation(generator, r, group));
        return Pair.of(pubKey, r);
    }

    public static BigInteger getJointKey(BigInteger group, BigInteger ourRandomNum, ElGamalPair theirPubKey) {
        return fastExponentiation(theirPubKey.getEncryptionKey(), ourRandomNum, group);
    }

    public static BigInteger encrypt(BigInteger message, BigInteger group, BigInteger ourRandomNum, ElGamalPair othersPubKey) {
        assert(othersPubKey.getGroup().equals(group));
        BigInteger jointKey = getJointKey(group, ourRandomNum, othersPubKey);
        return message.multiply(jointKey).mod(group);
    }

    public static BigInteger decrypt(BigInteger encryptedMessage, BigInteger group, BigInteger ourRandomNum, ElGamalPair othersPubKey) {
        BigInteger jointKey = getJointKey(group, ourRandomNum, othersPubKey);
        BigInteger inverse = fastExponentiation(jointKey, group.subtract(TWO), group);
        return inverse.multiply(encryptedMessage).mod(group);
    }

    public static BigInteger eavesdrop(BigInteger encryptedMessage, ElGamalPair alicePubKey, ElGamalPair bobPubKey) {
        BigInteger group = alicePubKey.getGroup();
        BigInteger generator = alicePubKey.getGenerator();
        BigInteger aliceKey = alicePubKey.getEncryptionKey();
        BigInteger bobKey = bobPubKey.getEncryptionKey();

        if (!generator.equals(bobPubKey.getGenerator())) {
            throw new RuntimeException("Generators do not match!  Unable to eavesdrop.");
        }

        if (!group.equals(bobPubKey.getGroup())) {
            throw new RuntimeException("Group is not the same!  Unable to eavesdrop.");
        }

        BigInteger recoveredAliceSecret = LittleStepGiantStep.findDiscreteLog_B_Of_A_InZ_P(generator, aliceKey, group);
        BigInteger recoveredBobSecret = LittleStepGiantStep.findDiscreteLog_B_Of_A_InZ_P(generator, bobKey, group);

        BigInteger jointKey = fastExponentiation(generator, recoveredAliceSecret.multiply(recoveredBobSecret), group);
        BigInteger inverse = fastExponentiation(jointKey, group.subtract(TWO), group);

        return encryptedMessage.multiply(inverse).mod(group);
    }
}
