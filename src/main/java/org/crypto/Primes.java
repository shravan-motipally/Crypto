package org.crypto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.String.format;

public class Primes {

    /**
     * @param n - Number (n) that which we are taking phi(n) of.
     * @param primeFactorization Maps p (prime, long) to e (exponent, integer)
     * @return
     */
    public static long phi(long n, Map<Long, Integer> primeFactorization) {
       System.out.println(format("Taking Phi(%d)", n));
       System.out.println();
       StringBuilder primeBuilder = new StringBuilder("Prime factorization is: ");
       StringBuilder phiExpansionBuilder = new StringBuilder(format("Phi calculation: Phi(%d) = ", n));
       AtomicLong result = new AtomicLong(1);
       primeFactorization.forEach((prime, exponent) -> {
           primeBuilder.append("(" + prime + "^" + exponent + ")");
           phiExpansionBuilder.append("(" + prime + " - 1)" + prime + "^(" + exponent + " - 1) ");
           long tmpResult = result.get() * (long)((prime - 1) * Math.pow(prime,exponent - 1));
           result.set(tmpResult);
       });
       phiExpansionBuilder.append(format(" = %d\n", result.get()));
       System.out.println(primeBuilder);
       System.out.println(phiExpansionBuilder);
       return result.get();
    }

    public static List<Integer> getPrimesUpTo(int n, boolean displayFactorsUptoN) throws Exception {
        if (n == Integer.MAX_VALUE) {
            throw new Exception("Cannot get primes upto this number as n+1 is bigger than max integer value");
        }
        boolean[] isPrime = new boolean[n+1];
        // isPrime[0], [1] means nothing.
        for (int i = 2; i < isPrime.length; i++) {
            isPrime[i] = true;
        }
        List<Integer> primes = new ArrayList<>();
        for (int j = 2; j <= n; j++) {
            if (isPrime[j]) {
                primes.add(j);
                if (displayFactorsUptoN) {
                    System.out.printf("%d is a prime.\n", j);
                }
                for (int i = 2*j; i <= n; i += j) {
                    isPrime[i] = false;
                }
            }
        }
        return primes;
    }

    public static Map<Long, Integer> getPrimeFactorization(long n) throws Exception {
        if (Math.sqrt(n) > Integer.MAX_VALUE) {
            throw new Exception("Too big of a value to get proper factors of using integer math.");
        }
        List<Integer> primes = getPrimesUpTo((int) Math.sqrt(n)+1, false);
        Map<Long, Integer> primeFactorization = new HashMap<>();
        long quotient = n;
        for (int i = 0; i < primes.size(); i++) {
            long currentPrime = primes.get(i);
            if (quotient % currentPrime == 0) { // is a prime factor
                primeFactorization.put(currentPrime, 0);
                while (quotient % currentPrime == 0) {
                    quotient = quotient / currentPrime;
                    primeFactorization.put(currentPrime, primeFactorization.get(currentPrime) + 1);
                }
            } // else continue
        }
        System.out.printf("Prime factorization of is %d = ", n);
        if (!primeFactorization.isEmpty()) {
            AtomicLong multiple = new AtomicLong(1L);
            primeFactorization.forEach((prime, exponent) -> {
                System.out.printf("(%d ^ %d)", prime, exponent);
                multiple.updateAndGet(v -> (long) (v * Math.pow(prime, exponent)));
            });

            if (n/multiple.get() != 1) {
                System.out.printf("(%d ^ 1)", n/multiple.get());
                primeFactorization.put(multiple.get(), 1);
            }
        } else { // meaning it couldn't find any prime factors between 1 and sqrt(n).  Therefore, n has to be a prime.
            System.out.printf("(%d ^ 1)", n);
            primeFactorization.put(n, 1);
        }

        System.out.println();
        return primeFactorization;
    }
}
