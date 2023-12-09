package org.crypto;

import model.ElGamalPair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import java.math.BigInteger;

import static java.math.BigInteger.TWO;
import static java.math.BigInteger.valueOf;
import static org.crypto.FastExponentiation.fastExponentiation;
import static org.junit.jupiter.api.Assertions.*;

public class LittleStepGiantStepTest {


    @Test
    public void testLSGS() throws Exception {
//        LittleStepGiantStep.findDiscreteLog_B_Of_A_InZ_P(2, 3, 29);
        LittleStepGiantStep.findDiscreteLog_B_Of_A_InZ_P(2, 13, 29);
    }

    @Test
    public void testLSGSViaElGamal() {
        BigInteger group = valueOf(10901741);
        // we assume that the group (prime) is sniffed by MiTM attack.
        BigInteger generator = new BigInteger("7200621");
//        Pair<ElGamalPair, BigInteger> publicKeyAndSecret = ElGamal.generatePublicKeyAndSecret(group);
//        ElGamalPair publicKey = new ElGamalPair(group, generator, new BigInteger("1378805"));
//        BigInteger secret = publicKeyAndSecret.getRight(); // attacker does not have this info.
        BigInteger recoveredSecret = LittleStepGiantStep.findDiscreteLog_B_Of_A_InZ_P(generator,
                new BigInteger("7106903"), group);
        System.out.println(recoveredSecret);
//        assertEquals(secret, recoveredSecret);
    }

    @Test
    public void testLSGSViaElGamalAsEve() {
        // we assume that the group (prime) is sniffed by MiTM attack.
        BigInteger prime = valueOf(16396147L);
        BigInteger generator = new BigInteger("2");
        BigInteger encryptedMessage = new BigInteger("14521569");
        BigInteger bobKey = new BigInteger("15682381");
        ElGamalPair publicKey = new ElGamalPair(prime, generator, new BigInteger("7197288"));

        BigInteger recoveredAliceSecret = LittleStepGiantStep.findDiscreteLog_B_Of_A_InZ_P(generator, publicKey.getEncryptionKey(), prime);
        BigInteger recoveredBobSecret = LittleStepGiantStep.findDiscreteLog_B_Of_A_InZ_P(generator, bobKey, prime);

        BigInteger jointKey = fastExponentiation(generator, recoveredAliceSecret.multiply(recoveredBobSecret), prime);
        BigInteger inverse = fastExponentiation(jointKey, prime.subtract(TWO), prime);
        System.out.println(encryptedMessage.multiply(inverse).mod(prime));

//        assertEquals(secret, recoveredSecret);
    }
}