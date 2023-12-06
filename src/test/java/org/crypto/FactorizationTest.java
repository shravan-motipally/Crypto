package org.crypto;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Map;

import static org.crypto.Factorization.*;
import static org.crypto.Primes.generateLargePrimeWithNBits;
import static org.crypto.Primes.getPrimeFactorizationForPrimesLessThan1000;
import static org.junit.jupiter.api.Assertions.*;

public class FactorizationTest {

    @Test
    public void ensureThatMultipleLogicWorksCorrectly() {
        Map<BigInteger, Integer> factorization = getPrimeFactorizationForPrimesLessThan1000(BigInteger.valueOf(2018));
        assertEquals(getMultiple(factorization), BigInteger.valueOf(2));
    }

    @Test
    public void factorizationRhoPollardTest() {
        System.out.printf("%s", rhoFactorization(BigInteger.valueOf(387400807)));
    }

    @Test
    public void getAllFactorsTestUsingPollardTest() {
        Map<BigInteger, Integer> allFactors = findAllFactorsUsingRhoFactorization(BigInteger.valueOf(387400807));
        assertTrue(allFactors.containsKey(BigInteger.valueOf(19441)));
        assertTrue(allFactors.containsKey(BigInteger.valueOf(19927)));
    }

    @Test
    public void getAllFactorsForAllThreeTypesOfNumbersSmallBigLarge() {
        Map<BigInteger, Integer> allFactors = findAllFactorsUsingRhoFactorization(new BigInteger("35465454364532435346577365432357345412434562"));
        assertTrue(allFactors.containsKey(new BigInteger("1695389512944339875734872088597")));
        assertTrue(allFactors.containsKey(BigInteger.valueOf(7858288783L)));
        assertTrue(allFactors.containsKey(BigInteger.valueOf(11)));
        assertTrue(allFactors.containsKey(BigInteger.valueOf(2)));
    }

    @Test
    public void getAllFactorsForTwoMultiple() {
        Map<BigInteger, Integer> allFactors = findAllFactorsUsingRhoFactorization(BigInteger.TWO.pow(1024));
        assertTrue(allFactors.containsKey(BigInteger.TWO));
        assertTrue(allFactors.get(BigInteger.TWO).equals(1024));
    }

    @Test
    public void getAllFactorsForTwoLargePrimesWithLargeMultiple() {
        BigInteger prime1 = generateLargePrimeWithNBits(24);
        BigInteger prime2 = generateLargePrimeWithNBits(24);
        Map<BigInteger, Integer> allFactors = findAllFactorsUsingRhoFactorization(BigInteger.TWO.pow(1024).multiply(prime1).multiply(prime2));
        assertTrue(allFactors.containsKey(BigInteger.TWO));
        assertTrue(allFactors.get(BigInteger.TWO).equals(1024));
        assertTrue(allFactors.containsKey(prime1));
        assertTrue(allFactors.containsKey(prime2));
    }
}