package org.crypto;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PrimesTest {

    @Test
    public void phiTest() throws Exception {
        //assertEquals(Primes.phi(20, new HashMap<>(){{ put(2L, 2); put(5L, 1); }}), 8L);
        long numberToTest = 100L;
        Primes.phi(numberToTest, Primes.getPrimeFactorization(numberToTest));
    }

    @Test
    public void getPrimesTest() throws Exception {
        List<Integer> primes = Primes.getPrimesUpTo(10000, true);
        for (Integer prime : primes) {
            Map<Long, Integer> primeFactorization = Primes.getPrimeFactorization((long) prime);
            assertTrue(primeFactorization.size() == 1);
        }
    }

    @Test
    public void getPrimeFactorization() throws Exception {
        //Primes.findProperFactorsOf(111111111111111L);
        //Primes.findProperFactorsOf(110111101111011L);
        Primes.getPrimeFactorization(161);
        //Primes.getPrimeFactorization(101010101010101L);
    }

    @Test
    public void getPrimeFactorizationForComposites() throws Exception {
        Primes.getPrimeFactorization(98724071000L);
    }
}