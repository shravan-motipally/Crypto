package org.crypto;

import model.ElGamalPair;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;

import static java.math.BigInteger.TWO;
import static org.crypto.FastExponentiation.fastExponentiation;
import static org.crypto.PrimitiveRootSearch.primitiveRootSearch;

public class ElGamal {

    public static BigInteger generateCommonGroup(int maxBits) {
        return Primes.generateLargePrimeWithNBits(maxBits);
    }

    public static ElGamalPair generatePublic(BigInteger group) {
        BigInteger primitiveRoot = primitiveRootSearch(group);
        BigInteger r = Utils.randomBigIntegerWithin(group);
        ElGamalPair pubKey = new ElGamalPair(group, primitiveRoot, fastExponentiation(primitiveRoot, r, group));
        return pubKey;
    }

    public static BigInteger getJointKey(BigInteger group, ElGamalPair pubKey) {
        BigInteger l = /* change once received */ Utils.randomBigIntegerWithin(group);
        BigInteger jointKey = fastExponentiation(pubKey.getEncryptionKey(), l, group);
        return jointKey;
    }

    public static BigInteger encrypt(BigInteger message, BigInteger group, ElGamalPair othersPubKey) {
        assert(othersPubKey.getGroup().equals(group));
        BigInteger jointKey = getJointKey(group, othersPubKey);
        return message.multiply(jointKey);
    }

    public static BigInteger decrypt(BigInteger encryptedMessage, BigInteger group, ElGamalPair othersPubKey) {
        return null;
    }
}
