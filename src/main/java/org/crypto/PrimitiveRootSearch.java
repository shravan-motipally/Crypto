package org.crypto;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.math.BigInteger.ONE;
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

    public static BigInteger primitiveRootSearch(BigInteger p) {
        boolean needToDiscardValue = false;
        BigInteger b;
        do {
            // bbg is slow but works
//            b = RandomGenerator.bbgGenerator(n.bitLength());
            b = Utils.randomBigIntegerWithin(p);
            needToDiscardValue = false;
            System.out.println("Number generated: " + b);
            Map<BigInteger, Integer> primeFactorization = Factorization.findAllFactorsUsingRhoFactorization(
                    Primes.phi(p, Factorization.findAllFactorsUsingRhoFactorization(p))
            );

            for (Map.Entry<BigInteger, Integer> entry : primeFactorization.entrySet()) {
                BigInteger q = entry.getKey();
                BigInteger res = fastExponentiation(b, p.subtract(ONE).divide(q), p);
                if (res.equals(ONE)) {
                    needToDiscardValue = true;
                    break;
                }
            }
        } while (needToDiscardValue);
        return b;
    }
}
