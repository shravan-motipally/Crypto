package org.crypto;

import model.ElGamalPair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import java.math.BigInteger;

import static java.math.BigInteger.TWO;
import static org.junit.jupiter.api.Assertions.*;

public class LittleStepGiantStepTest {


    @Test
    public void testLSGS() throws Exception {
//        LittleStepGiantStep.findDiscreteLog_B_Of_A_InZ_P(2, 3, 29);
        LittleStepGiantStep.findDiscreteLog_B_Of_A_InZ_P(2, 13, 29);
    }

    @Test
    public void testLSGSViaElGamal() {
        BigInteger prime = Primes.generateLargePrimeWithNBits(24);
        // we assume that the group (prime) is sniffed by MiTM attack.
        Pair<ElGamalPair, BigInteger> publicKeyAndSecret = ElGamal.generatePublicKeyAndSecret(prime);
        ElGamalPair publicKey = publicKeyAndSecret.getLeft();
        BigInteger secret = publicKeyAndSecret.getRight(); // attacker does not have this info.
        BigInteger recoveredSecret = LittleStepGiantStep.findDiscreteLog_B_Of_A_InZ_P(publicKey.getGenerator(), publicKey.getEncryptionKey(), prime);
        assertEquals(secret, recoveredSecret);
    }
}