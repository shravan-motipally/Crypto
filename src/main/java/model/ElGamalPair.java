package model;

import java.math.BigInteger;

public class ElGamalPair {
    private final BigInteger group;
    private final BigInteger generator;
    private final BigInteger encryptionKey;

    public ElGamalPair(BigInteger group, BigInteger generator, BigInteger encryptionKey) {
        this.group = group;
        this.generator = generator;
        this.encryptionKey = encryptionKey;
    }

    public BigInteger getEncryptionKey() {
        return encryptionKey;
    }

    public BigInteger getGenerator() {
        return generator;
    }

    public BigInteger getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return "El Gamal[Group=" + group + "," +
                " generator: " + generator + "," +
                " encryptionKey: " + encryptionKey + ".";
    }
}
