package org.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.String.format;
import static java.math.BigInteger.*;
import static org.crypto.Factorization.getMultiple;
import static org.crypto.FastExponentiation.fastExponentiation;
import static org.crypto.Utils.randomBigIntegerWithin;

public class Primes {

    protected static HashMap<BigInteger, Boolean> millerRabinStore = new HashMap<>();

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

    public static long phi(BigInteger n, Map<Long, Integer> primeFactorization) {
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

    public static Map<BigInteger, Integer> getPrimeFactorizationForPrimesLessThan1000(BigInteger n) {
        List<Integer> primes = new LinkedList<>();
        try {
            primes = getPrimesUpTo(1000, false);
        } catch (Exception e) {
            throw new RuntimeException("Getting primes upto 1000 throw an error");
        }
        Map<BigInteger, Integer> primeFactorization = new HashMap<>();
        BigInteger quotient = n;
        for (int i = 0; i < primes.size(); i++) {
            BigInteger currentPrime = BigInteger.valueOf(primes.get(i));
            if (quotient.mod(currentPrime).equals(ZERO)) { // is a prime factor
                primeFactorization.put(currentPrime, 0);
                while (quotient.mod(currentPrime).equals(ZERO)) {
                    quotient = quotient.divide(currentPrime);
                    primeFactorization.put(currentPrime, primeFactorization.get(currentPrime) + 1);
                }
            } // else continue
        }
        System.out.printf("Prime factorization of %s is = ", n);
        return primeFactorization;
    }

    public static boolean isPrimeMillerRabin(BigInteger primeToCheck, int numberOfTrials) {
        if (millerRabinStore.containsKey(primeToCheck)) {
            return millerRabinStore.get(primeToCheck);
        }

        if (primeToCheck.mod(TWO).equals(ZERO)) {
            System.out.printf("Given number %s is even, so its not a prime\n", primeToCheck);
            millerRabinStore.put(primeToCheck, false);
            return false;
        }
        if (primeToCheck.compareTo(ZERO) <= 0) {
            System.out.println("Negative number entered, bad input.");
            millerRabinStore.put(primeToCheck, false);
            return false;
        }
        System.out.printf("Starting miller rabin primality test.\n n = %s\n", primeToCheck);
        // now we know that the number is odd.
        BigInteger m = primeToCheck.subtract(ONE);
       // System.out.printf("n-1 = %s\n", m);
        BigInteger r = ZERO;
        System.out.printf("Figuring out the number of 2 factors that go into %s\n", m);
        while (m.mod(TWO).equals(ZERO)) {
            r = r.add(ONE);
            m = m.divide(TWO);
        }
        assert(!m.mod(TWO).equals(ZERO));
        System.out.printf("%s - 1 = 2 ^ %s * %s\n", primeToCheck, r, m);
        // by now we have r and m.  We begin miller rabin.
        for (int i = 0; i < numberOfTrials; i++) {
            BigInteger b = randomBigIntegerWithin(primeToCheck.subtract(ONE));  // randomly generate number between 1 and primeToCheck - 1;

            System.out.printf("Generated random number between 1 and %s = %s\n", primeToCheck.subtract(ONE), b);
            // first step
            BigInteger res = fastExponentiation(b, m, primeToCheck);
            //System.out.printf("%s ^ %s mod %s = %s\n", b, m, primeToCheck, res);

            // second step
            if (res.equals(ONE) || res.equals(primeToCheck.subtract(ONE)) || res.equals(BigInteger.valueOf(-1))) {
                //System.out.printf("One of the following is true %s = 1\n%s = %s - 1\n%s = -1\nSo we stop.\n", res, res, primeToCheck, res);
                millerRabinStore.put(primeToCheck, true);
                return true;
            } else {
                //System.out.printf("%s != 1\n%s != %s - 1\n%s != -1\nSo we continue...\n", res, res, primeToCheck, res);
            }

            for (BigInteger s = ONE; s.compareTo(r.subtract(ONE)) <= 0; s = s.add(ONE)) {
               // System.out.printf("Trial #%s\n", s);

                BigInteger x = (b.multiply(b)).mod(primeToCheck);
                //System.out.printf("%s = %s^2 mod %s\n", x, b, primeToCheck);
                b = x;

                if (b.equals(primeToCheck.subtract(ONE)) || b.equals(BigInteger.valueOf(-1))) {
                    //System.out.printf("%s is either equal to %s - 1 or -1\nTherefore, it is probably prime.", b, primeToCheck);
                    millerRabinStore.put(primeToCheck, true);
                    return true;
                } else if (b.equals(ONE)) {
                    //System.out.printf("%s is equal to 1\nTherefore, it is composite.", b, primeToCheck);
                    millerRabinStore.put(primeToCheck, false);
                    return false;
                }
            }
        }
        System.out.println("None of the trials have given a suitable result.  Therefore we cannot conclude it is a prime.");
        millerRabinStore.put(primeToCheck, false);
        return false;
    }

    private static boolean isNumberDivisibleWithinPrimesLessThan1000(BigInteger n)  {
        try {
            List<Integer> primesLessThan1000 = getPrimesUpTo(1000, false);
            boolean foundFactor = false;
            for (int i = 0; i < primesLessThan1000.size(); i++) {
                foundFactor = n.mod(BigInteger.valueOf(primesLessThan1000.get(i))).equals(ZERO);
                if (foundFactor) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error trying to fetch primes");
            return false;
        }
    }

    public static BigInteger generateLargePrimeWithNBits(int n) {
        BigInteger testNumber;
        BigInteger tries = TWO.pow(n).subtract(TWO.pow(n-1));
        System.out.println("Starting to generate large prime number");
        System.out.println("# of tries: " + tries);
        do {
            testNumber = randomBigIntegerWithin(TWO.pow(n-1), TWO.pow(n));
            System.out.println();
            System.out.printf("Number generated: %s\n", testNumber);
            if (testNumber.mod(TWO).equals(ZERO)) {
                testNumber = testNumber.subtract(ONE); // make it odd.
               // System.out.printf("Even # generated, so subtracting one. Number: %s\n", testNumber);
            }
            tries.subtract(ONE);
            System.out.printf("Try #: %s\n", tries);
            System.out.printf("tries.compareTo(ZERO) > 0: %b & ", tries.compareTo(ZERO) > 0);
            System.out.printf("isNumberDivisibleWithinPrimesLessThan1000: %b & ", isNumberDivisibleWithinPrimesLessThan1000(testNumber));
            System.out.printf("!isPrimeMillerRabinTest(testNumber, 5): %b\n", !isPrimeMillerRabin(testNumber, 5));
        } while (tries.compareTo(ZERO) > 0
                && (isNumberDivisibleWithinPrimesLessThan1000(testNumber)
                || !isPrimeMillerRabin(testNumber, 5)));

        if (tries.equals(ZERO)) {
            // at this point - most likely trials have not found a result.
            System.err.println("Trials have concluded with no prime number.");
            return BigInteger.valueOf(-1);
        }
        return testNumber;
    }
}
