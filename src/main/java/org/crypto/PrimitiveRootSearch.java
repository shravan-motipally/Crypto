package org.crypto;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.crypto.FastExponentiation.fastExponentiation;

public class PrimitiveRootSearch {

    public static boolean testPrimitiveRoot(Long n, Long valueToTest) throws Exception {
        Map<Long, Integer> primeFactorization = Primes.getPrimeFactorization(Primes.phi(n, Primes.getPrimeFactorization(n)));
        AtomicBoolean isPrimitiveRoot = new AtomicBoolean(true);
        primeFactorization.forEach((prime, __) -> {
            long res = fastExponentiation(valueToTest, (n-1)/prime, n);
            if (res == 1) {
                isPrimitiveRoot.set(false);
            }
        });
        return isPrimitiveRoot.get();
    }
}
