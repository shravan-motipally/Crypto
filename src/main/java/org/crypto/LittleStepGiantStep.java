package org.crypto;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static org.crypto.Primes.isPrimeMillerRabin;
import static org.crypto.Utils.DOUBLE_TAB;

public class LittleStepGiantStep {

    public static long findDiscreteLog_B_Of_A_InZ_P(long b, long a, long p) throws Exception {
        Map<Long, Integer> primeFactorization = Primes.getPrimeFactorization(p);
        if (primeFactorization.size() != 1) // check to see if p is infact a prime otherwise this doesn't work.
        {
            throw new Exception(format("%d given not a prime value.", p));
        }
        // now we know its a prime.
        long phi = p - 1;
        System.out.printf("Checked that %d was a prime, therefore phi(%d) = %d\n", p, p, phi);
        long m = (long) Math.ceil(Math.sqrt(phi));
        System.out.printf("Compute (%d)^j for 0 <= j < m where m = sqrt(%d -1) = %d\n", b, p, m);

        Map<Long, Long> bjToJMapping = new HashMap<>();
        System.out.println(DOUBLE_TAB + "j" + DOUBLE_TAB + "b^j");
        for (long j = 0; j < m; j++) {
            long exp = (long) (Math.pow(b, j) % p);
            System.out.printf(DOUBLE_TAB + "%d" + DOUBLE_TAB + "%d^%d = %d \n", j, b, j, exp);
            bjToJMapping.put(exp, j);
        }
        System.out.println();
        System.out.printf("Now finding inverse of %d in Z_%d\n", b, p);
        Pair<Long, Long> inverseCalculationXYForBandP = Euclidean.findXYForExtendedEuclidean(b, p);
        long inverseOfBinZ_P = inverseCalculationXYForBandP.getLeft();
        System.out.printf("Inverse =  %d\n", inverseOfBinZ_P);
        System.out.printf("Now performing %d ^ %d\n", inverseOfBinZ_P, m);
        long c = FastExponentiation.fastExponentiation(inverseOfBinZ_P, m, p);

        Map<Long, Long> iToCjMapping = new HashMap<>();
        List<Pair<Long, Long>> matchesFoundInIJFormat = new ArrayList<>();
        System.out.println();
        long temp = a;
        for (long i = 0; i < m; i++) {
            System.out.printf(DOUBLE_TAB + "%d" + DOUBLE_TAB + "%d * %d ^ %d = ", i, a, c, i);
            if (i != 0) {
                temp = (temp * c) % p;
            }
            iToCjMapping.put(i, temp);
            if (bjToJMapping.containsKey(temp)) {
                matchesFoundInIJFormat.add(Pair.of(i, bjToJMapping.get(temp)));
            }
            System.out.printf("%d" + DOUBLE_TAB + "%b\n", temp, bjToJMapping.containsKey(temp));
        }
        if (matchesFoundInIJFormat.isEmpty()) {
            System.out.println("No matches found.");
            return -1;
        } else {
            Pair<Long, Long> firstPair = matchesFoundInIJFormat.get(0);
            long l = (firstPair.getLeft() * m + firstPair.getRight()) % phi;
            System.out.println("i" + DOUBLE_TAB + "*" + DOUBLE_TAB + "m" + DOUBLE_TAB + "+" + DOUBLE_TAB + "j mod phi= ");
            for (int i = 0; i < matchesFoundInIJFormat.size(); i++) {
                long tempVal = (matchesFoundInIJFormat.get(i).getLeft() * m + matchesFoundInIJFormat.get(i).getRight()) % phi;
                System.out.printf("%d" + DOUBLE_TAB + "*" + DOUBLE_TAB + "%d" + DOUBLE_TAB + "+" + DOUBLE_TAB + "%d mod %d = %d\n",
                        matchesFoundInIJFormat.get(i).getLeft(), m, matchesFoundInIJFormat.get(i).getRight(), phi, tempVal);
            }
            return l;
        }
    }

    public static BigInteger findDiscreteLog_B_Of_A_InZ_P(BigInteger b, BigInteger a, BigInteger p) {
        if (!isPrimeMillerRabin(p, 5)) // check to see if p is in fact a prime otherwise this doesn't work.
        {
            throw new RuntimeException(format("%d given not a prime value.", p));
        }
        // now we know its a prime.
        BigInteger phi = p.subtract(ONE);
        System.out.printf("Checked that %d was a prime, therefore phi(%d) = %d\n", p, p, phi);
        BigInteger[] mAndR = phi.sqrtAndRemainder();
        BigInteger m = mAndR[1].compareTo(ZERO) >= 0 ? mAndR[0].add(ONE) : mAndR[0]; // does ceil(sqrt)

        System.out.printf("Compute (%s)^j for 0 <= j < m where m = sqrt(%s -1) = %d\n", b, p, m);

        Map<BigInteger, BigInteger> bjToJMapping = new HashMap<>();
        System.out.println(DOUBLE_TAB + "j" + DOUBLE_TAB + "b^j");
        for (BigInteger j = ZERO; j.compareTo(m) < 0; j = j.add(ONE)) {
            BigInteger exp = FastExponentiation.fastExponentiation(b, j, p);
            System.out.printf(DOUBLE_TAB + "%d" + DOUBLE_TAB + "%d^%d = %d \n", j, b, j, exp);
            bjToJMapping.put(exp, j);
        }
        System.out.println();
        System.out.printf("Now finding inverse of %d in Z_%d\n", b, p);
        Pair<BigInteger, BigInteger> inverseCalculationXYForBandP = Euclidean.findXYForExtendedEuclidean(b, p);
        BigInteger inverseOfBinZ_P = inverseCalculationXYForBandP.getLeft();
        System.out.printf("Inverse =  %d\n", inverseOfBinZ_P);
        System.out.printf("Now performing %d ^ %d\n", inverseOfBinZ_P, m);
        BigInteger c = FastExponentiation.fastExponentiation(inverseOfBinZ_P, m, p);

        Map<BigInteger, BigInteger> iToCjMapping = new HashMap<>();
        List<Pair<BigInteger, BigInteger>> matchesFoundInIJFormat = new ArrayList<>();
        System.out.println();
        BigInteger temp = a;
        for (BigInteger i = ZERO; i.compareTo(m) < 0; i = i.add(ONE)) {
            System.out.printf(DOUBLE_TAB + "%s" + DOUBLE_TAB + "%s * %s ^ %s = ", i, a, c, i);
            if (!i.equals(ZERO)) {
                temp = temp.multiply(c).mod(p);
            }
            iToCjMapping.put(i, temp);
            if (bjToJMapping.containsKey(temp)) {
                matchesFoundInIJFormat.add(Pair.of(i, bjToJMapping.get(temp)));
            }
            System.out.printf("%d" + DOUBLE_TAB + "%b\n", temp, bjToJMapping.containsKey(temp));
        }
        if (matchesFoundInIJFormat.isEmpty()) {
            System.out.println("No matches found.");
            throw new RuntimeException("No discrete log found.");
        } else {
            Pair<BigInteger, BigInteger> firstPair = matchesFoundInIJFormat.get(0);
            BigInteger l = (firstPair.getLeft().multiply(m).add(firstPair.getRight())).mod(phi);
            System.out.println("i" + DOUBLE_TAB + "*" + DOUBLE_TAB + "m" + DOUBLE_TAB + "+" + DOUBLE_TAB + "j mod phi= ");
            for (int i = 0; i < matchesFoundInIJFormat.size(); i++) {
                BigInteger tempVal = (matchesFoundInIJFormat.get(i).getLeft().multiply(m).add(matchesFoundInIJFormat.get(i).getRight())).mod(phi);
                System.out.printf("%d" + DOUBLE_TAB + "*" + DOUBLE_TAB + "%d" + DOUBLE_TAB + "+" + DOUBLE_TAB + "%d mod %d = %d\n",
                        matchesFoundInIJFormat.get(i).getLeft(), m, matchesFoundInIJFormat.get(i).getRight(), phi, tempVal);
            }
            return l;
        }
    }
}
