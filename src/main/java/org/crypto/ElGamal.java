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

    public static BigInteger findEncryptionKey(ElGamalPair pair, int maxBits) {
        if (pair.getEncryptionKey().equals(pair.getGenerator())) {
            return ONE;
        }

        BigInteger max = TWO.pow(maxBits);
        BigInteger currentRand;

        do {
           currentRand = Utils.randomBigIntegerWithin(max);

        } while (ONE.equals(TWO));
        return null;
    }
}
