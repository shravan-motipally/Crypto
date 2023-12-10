package org.crypto;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PrimesTest {

    @Test
    public void phiTest() throws Exception {
        long numberToTest = 100L;
        assertEquals(40, Primes.phi(numberToTest, Primes.getPrimeFactorization(numberToTest)));
    }

    @Test
    public void getPrimesTest() throws Exception {
        List<Integer> primes = Primes.getPrimesUpTo(10000, true);
        for (Integer prime : primes) {
            Map<Long, Integer> primeFactorization = Primes.getPrimeFactorization((long) prime);
            assertEquals( 1, primeFactorization.size());
        }
    }

    @Test
    public void testPrimeFactorizationForComposites() throws Exception {
        BigInteger composite = BigInteger.valueOf(98724071000L);
        Map<Long, Integer> factorization = Primes.getPrimeFactorization(98724071000L);
        Map<BigInteger, Integer> factors = new HashMap<>();
        factorization.forEach((key, value) -> factors.put(BigInteger.valueOf(key), value));
        assertEquals(composite, Factorization.getMultiple(factors));
    }

    @Test
    public void testPrimalityHappyPath() {
        assertTrue(Primes.isPrimeMillerRabin(BigInteger.valueOf(100000003709L), 5));
    }

    @Test
    public void testPrimalityWithEvenNumbers() {
        assertFalse(Primes.isPrimeMillerRabin(BigInteger.TWO.pow(20), 5));
    }

    @Test
    public void testPrimalityWithNegativeNumber() {
        assertFalse(Primes.isPrimeMillerRabin(BigInteger.valueOf(-1), 2));
    }

    @Test
    public void testPrimeGenerationLogic() {
        for (int i = 0; i < 10; i++) {
            assertTrue(Primes.isPrimeMillerRabin(BigInteger.valueOf(16396147), 10));
        }
    }

    @Test
    public void testPrimalityCache() {
        boolean isPrime = Primes.isPrimeMillerRabin(BigInteger.valueOf(100000003709L), 5);
        assertTrue(Primes.millerRabinStore.containsKey(BigInteger.valueOf(100000003709L)));
        assertEquals(isPrime,Primes.millerRabinStore.get(BigInteger.valueOf(100000003709L)));
    }
}