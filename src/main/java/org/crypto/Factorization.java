package org.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.math.BigInteger.*;
import static org.crypto.Euclidean.findGcdUsingEuclidean;
import static org.crypto.Primes.*;

public class Factorization {

    public static BigInteger rhoFactorization(BigInteger n) {
        if (n.compareTo(ZERO) <= 0) {
            throw new RuntimeException("Invalid input to rho factorization, give a number > 0");
        }
        // we test whether n is prime.
        boolean isPrime = isPrimeMillerRabin(n, 5);
        if (isPrime) {
            return n;
        }
        BigInteger x = TWO;
        BigInteger y = yFunc(x, n);
        BigInteger g = findGcdUsingEuclidean(x.subtract(y).abs(), n).getLeft();

        BigInteger maxTries = n.sqrt().sqrt().multiply(BigInteger.valueOf(100));

        if (g.equals(n)) {
            System.out.printf("Reinitializing because g = n, g = %s, n = %s\n", g, n);
            x = reinitialize(x);
            y = yFunc(x, n);
            g = findGcdUsingEuclidean(x.subtract(y).abs(), n).getLeft();
        }

        while (g.equals(ONE) && maxTries.compareTo(ZERO) > 0) {
            x = yFunc(x, n);
            y = yFunc(yFunc(y, n),n);
            g = findGcdUsingEuclidean(x.subtract(y).abs(), n).getLeft();
            System.out.printf("x: %s, y: %s, g: %s\n", x, y, g);


            if (g.equals(n)) {
                x = reinitialize(x);
                y = yFunc(x, n);
                g = findGcdUsingEuclidean(x.subtract(y).abs(), n).getLeft();
            }
            maxTries = maxTries.subtract(ONE);
        }

        if (maxTries.equals(ZERO)) {
            throw new RuntimeException("Max tries reached - reinitilization may not help.");
        }
        return g;
    }

    protected static BigInteger getMultiple(Map<BigInteger, Integer> factorization) {
        BigInteger multiple = ONE;
        for (Map.Entry<BigInteger, Integer> entry : factorization.entrySet()) {
            BigInteger prime = entry.getKey();
            Integer exponent = entry.getValue();
            multiple = multiple.multiply(prime.pow(exponent));
        }
        return multiple;
    }

    public static Map<BigInteger, Integer> findAllFactorsUsingRhoFactorization(BigInteger n)  {
        if (n.equals(ONE)) {
            // base case.
            return new HashMap<>();
        }
        Map<BigInteger, Integer> initialFactorization = getPrimeFactorizationForPrimesLessThan1000(n);
        BigInteger multiple = ONE;
        if (!initialFactorization.isEmpty()) {
            multiple = getMultiple(initialFactorization);
        }
        BigInteger remainder = n.divide(multiple);
        System.out.printf("After getting initial factors out, remainder is: %s, with multiple: %s - with factorization taken.\n", remainder, multiple);

        boolean isPrime = isPrimeMillerRabin(remainder,10 );
        if (isPrime) {
            System.out.printf("Remainder %s is a prime, so stopping here.\n", remainder);
            initialFactorization.put(remainder, initialFactorization.containsKey(remainder) ? initialFactorization.get(remainder) + 1 : 1);
            return initialFactorization;
        }

        BigInteger factor;

        factor = rhoFactorization(remainder);
        System.out.printf("Factor %s found!\n", factor);
        isPrime = isPrimeMillerRabin(factor, 5);
        if (isPrime) {
            System.out.printf("Turns out it (%s) was prime.\n", factor);
            initialFactorization.put(factor, initialFactorization.containsKey(factor) ? initialFactorization.get(factor) + 1 : 1);
        } else {
            System.out.printf("Turns out %s was not prime.\n", factor);
            Map<BigInteger, Integer> factorizationForFactor = findAllFactorsUsingRhoFactorization(factor);
            factorizationForFactor.forEach((fact, exponent) -> initialFactorization.merge(fact, exponent,
                    Integer::sum));
        }
        remainder = remainder.divide(factor);
        Map<BigInteger, Integer> factorizationForRemainder = findAllFactorsUsingRhoFactorization(remainder);
        factorizationForRemainder.forEach((fact, exponent) -> initialFactorization.merge(fact, exponent,
                Integer::sum));
        return initialFactorization;
    }

    private static BigInteger yFunc(BigInteger x, BigInteger mod) {
        return (x.multiply(x)).add(ONE).mod(mod)    ;
    }

    private static BigInteger reinitialize(BigInteger currentPrime) {
        try {
            List<Integer> primes = Primes.getPrimesUpTo(1000, false);
            for (int i = 0; i < primes.size(); i++) {
                if (currentPrime.equals(primes.get(i))) {
                    if (i == primes.size() -1) {
                        // reached the end
                        throw new RuntimeException("Too many reinitialization, stopping..");
                    } else {
                        return BigInteger.valueOf(primes.get(i+1));
                    }
                }
            }
            throw new RuntimeException("Logic error, no prime matched to reinitialize");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
