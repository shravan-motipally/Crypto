package org.crypto;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
        Map<Long, Integer> primes = Primes.getPrimeFactorization(161);
        primes.forEach((factor, exp) -> {
            System.out.printf("(%d ^ %d)", factor, exp);
        });
        System.out.println();
        //Primes.getPrimeFactorization(101010101010101L);
    }

    @Test
    public void getPrimeFactorizationForComposites() throws Exception {
        Primes.getPrimeFactorization(98724071000L);
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
//        BigInteger prime = Primes.generateLargePrimeWithNBits(1024);
        assertTrue(Primes.isPrimeMillerRabin(BigInteger.valueOf(19441), 5));

        System.out.printf("\n\nPRIME:\n\n%s\n\n", 19441);
    }

    @Test
    public void testPrimalityCache() {
        boolean isPrime = Primes.isPrimeMillerRabin(BigInteger.valueOf(100000003709L), 5);
        assertTrue(Primes.millerRabinStore.containsKey(BigInteger.valueOf(100000003709L)));
        assertEquals(isPrime,Primes.millerRabinStore.get(BigInteger.valueOf(100000003709L)));
    }
}