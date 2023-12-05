package org.crypto;

import model.RSAPair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.crypto.RSA.generatePublicAndPrivateKey;
import static org.junit.jupiter.api.Assertions.*;

public class RSATest {

    @Test
    public void encipherTest() {

    }

    @Test
    public void generatePublicKey() {
        Pair<RSAPair, RSAPair> pair = generatePublicAndPrivateKey();
        System.out.println(pair.getLeft());
    }

    @Test
    public void generatePublicKeyWithMaximumBits() {
        Pair<RSAPair, RSAPair> pair = generatePublicAndPrivateKey(2048);
        System.out.println(pair.getLeft());
    }

}