package org.crypto;

import model.BigInteger2x2Matrix;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static org.crypto.Utils.DOUBLE_TAB;

public class Euclidean {
    private static final String EUCLIDEAN_TEMPLATE = "%d" + DOUBLE_TAB + "="
            + DOUBLE_TAB + "%d" + DOUBLE_TAB + "*"
            + DOUBLE_TAB + "%d" + DOUBLE_TAB +
            "+" + DOUBLE_TAB + "%d\n";

    private static final String EUCLIDEAN_TEMPLATE_BIG_INT = "%s" + DOUBLE_TAB + "="
            + DOUBLE_TAB + "%s" + DOUBLE_TAB + "*"
            + DOUBLE_TAB + "%s" + DOUBLE_TAB +
            "+" + DOUBLE_TAB + "%s\n";

    protected static final HashMap<String, Pair<BigInteger, List<BigInteger>>> euclideanStore = new HashMap<>();


    public static Pair<Integer, List<Integer>> findGcdUsingEuclidean(int m, int n) {
        // form = n = q*m + r
        if (m == n) {
            return Pair.of(m, new ArrayList<>() {{
                add(1);
            }});
        }
        int tmpN = Math.max(m, n);
        int tmpM = Math.min(m, n);
        System.out.printf("Starting Euclidean, n = %d, m = %d\n", tmpN, tmpM);
        // assert that n > m
        assert (tmpN > tmpM);
        // prep quotients's for extended euclidean
        ArrayList<Integer> quotients = new ArrayList<>();

        int r = 0;
        System.out.println("n" + DOUBLE_TAB + "="
                + DOUBLE_TAB + "q" + DOUBLE_TAB + "*"
                + DOUBLE_TAB + "m" + DOUBLE_TAB +
                "+" + DOUBLE_TAB + "r");
        do {
            r = tmpN % tmpM;
            int q = (tmpN - r) / tmpM;
            quotients.add(q);
            System.out.printf(EUCLIDEAN_TEMPLATE, tmpN, q, tmpM, r);
            tmpN = tmpM;
            tmpM = r;
        } while (r != 0);

        // at this point tmpN has last value of r.
        System.out.printf("GCD(%d, %d) = %d.\n", m, n, tmpN);
//        if (tmpN == 1) {
//            System.out.printf("Note: %d and %d are relatively prime.\n", m, n);
//        }
        return Pair.of(tmpN, quotients);
    }

    public static Pair<Long, List<Long>> findGcdUsingEuclidean(long m, long n) {
        if (m == 0) {
            return Pair.of(n, new ArrayList<>(){{ add(1L); }});
        }
        // form = n = q*m + r
        if (m == n) {
            return Pair.of(m, new ArrayList<>() {{
                add(1L);
            }});
        }
        long tmpN = Math.max(m, n);
        long tmpM = Math.min(m, n);
//        System.out.printf("Starting Euclidean, n = %d, m = %d\n", tmpN, tmpM);
        // assert that n > m
        assert (tmpN > tmpM);
        // prep quotients's for extended euclidean
        ArrayList<Long> quotients = new ArrayList<>();

        long r = 0;
        System.out.println("n" + DOUBLE_TAB + "="
                + DOUBLE_TAB + "q" + DOUBLE_TAB + "*"
                + DOUBLE_TAB + "m" + DOUBLE_TAB +
                "+" + DOUBLE_TAB + "r");
        do {
            r = tmpN % tmpM;
            long q = (tmpN - r) / tmpM;
            quotients.add(q);
//            System.out.printf(EUCLIDEAN_TEMPLATE, tmpN, q, tmpM, r);
            tmpN = tmpM;
            tmpM = r;
        } while (r != 0);

        // at this point tmpN has last value of r.
//        System.out.printf("GCD(%d, %d) = %d.\n", m, n, tmpN);
//        if (tmpN == 1) {
//            System.out.printf("Note: %d and %d are relatively prime.\n", m, n);
//        }
        return Pair.of(tmpN, quotients);
    }

    public static Pair<BigInteger, List<BigInteger>> findGcdUsingEuclidean(BigInteger m, BigInteger n) {
        if (m.equals(ZERO)) {
            List<BigInteger> list = new ArrayList<>() {{ add(ONE); }};
            return Pair.of(n, list);
        }
        String key = String.format("%s|%s", m, n);
        String reverseKey = String.format("%s|%s", n, m);

        if (euclideanStore.containsKey(key) || euclideanStore.containsKey(reverseKey)) {
            return euclideanStore.containsKey(key) ? euclideanStore.get(key) : euclideanStore.get(reverseKey);
        }

        // form = n = q*m + r
        if (m.equals(n)) {

            Pair<BigInteger, List<BigInteger>> res = Pair.of(m, new ArrayList<>() {{
                add(ONE);
            }});
            euclideanStore.put(key, res);
            euclideanStore.put(reverseKey, res);
            return res;
        }
        // we need N to be the larger of the two, so when we do our euclidean we do n = q * m + r.
        BigInteger tmpN = m.subtract(n).compareTo(ZERO) < 0 ? n : m;
        BigInteger tmpM = m.subtract(n).compareTo(ZERO) < 0 ? m : n;
//        System.out.printf("Starting Euclidean, n = %s, m = %s\n", tmpN, tmpM);
        // assert that n > m
        assert (tmpN.compareTo(tmpM) > 0);
        // prep quotients's for extended euclidean
        ArrayList<BigInteger> quotients = new ArrayList<>();

        BigInteger r = ZERO;
//        System.out.println("n" + DOUBLE_TAB + "="
//                + DOUBLE_TAB + "q" + DOUBLE_TAB + "*"
//                + DOUBLE_TAB + "m" + DOUBLE_TAB +
//                "+" + DOUBLE_TAB + "r");
        do {
            r = tmpN.mod(tmpM);
            BigInteger q = (tmpN.subtract(r)).divide(tmpM);
            quotients.add(q);
//            System.out.printf(EUCLIDEAN_TEMPLATE_BIG_INT, tmpN, q, tmpM, r);
            tmpN = tmpM;
            tmpM = r;
        } while (!r.equals(ZERO));

        // at this point tmpN has last value of r.
//        System.out.printf("GCD(%d, %d) = %d.\n", m, n, tmpN);
//        if (tmpN.equals(ONE)) {
//            System.out.printf("Note: %d and %d are relatively prime.\n", m, n);
//        }
        Pair<BigInteger, List<BigInteger>> res = Pair.of(tmpN, quotients);
        euclideanStore.put(key, res);
        euclideanStore.put(reverseKey, res);
        return res;
    }

    public static Pair<Integer, Integer> findXYForExtendedEuclidean(int m, int n) {
        // form = n = q*m + r
        if (m == n) {
            return Pair.of(2, -1);
        }
        int tmpN = Math.max(m, n);
        int tmpM = Math.min(m, n);
//        System.out.printf("Starting Extended Euclidean, n = %d, m = %d\n", tmpN, tmpM);
        // assert that n > m
        assert (tmpN > tmpM);
        Pair<Integer, List<Integer>> gcdValues = findGcdUsingEuclidean(tmpM, tmpN);
        List<Integer> quotients = gcdValues.getRight();
        int size = quotients.size();
        printQuotientMatrix(quotients);
//        System.out.println("-------------------");
        int currentIndex = size - 1;
        RealMatrix currentMultiple = getMatrixWithQuotient(quotients.get(currentIndex));
//        System.out.println("First coeff matrix\n" + getStringRep(currentMultiple));
//        quotients.forEach(i -> System.out.printf("%d ", i));
//        System.out.println();
        while (currentIndex > 0) {
//            System.out.printf("CurrentIndex: %d\n", currentIndex);
            currentIndex -= 1;
//            System.out.println("*");
            RealMatrix currentMatrix = getMatrixWithQuotient(quotients.get(currentIndex));
//            System.out.println(getStringRep(currentMatrix));
//            System.out.println("=");
            currentMultiple = currentMultiple.multiply(currentMatrix);
//            System.out.println(getStringRep(currentMultiple));

        }
//        System.out.println("After all multiplications, the quotient matrix is: ");
//        System.out.println(getStringRep(currentMultiple));

        int x = (int) currentMultiple.getRow(0)[0];
        int y = (int) currentMultiple.getRow(0)[1];

//        System.out.printf("x = %d, y = %d, therefore %d * %d + %d * %d = %d\n",
//                y, x, y, tmpM, x, tmpN, gcdValues.getLeft());
        return Pair.of(y, x);
    }

    public static Pair<Long, Long> findXYForExtendedEuclidean(long m, long n) {
        // form = n = q*m + r
        if (m == n) {
            return Pair.of(2L, -1L);
        }
        long tmpN = Math.max(m, n);
        long tmpM = Math.min(m, n);
        System.out.printf("Starting Extended Euclidean, n = %d, m = %d\n", tmpN, tmpM);
        // assert that n > m
        assert (tmpN > tmpM);
        Pair<Long, List<Long>> gcdValues = findGcdUsingEuclidean(tmpM, tmpN);
        List<Long> quotients = gcdValues.getRight();
        int size = quotients.size();
        printQuotientMatrixForLongs(quotients);
        System.out.println("-------------------");
        int currentIndex = size - 1;
        RealMatrix currentMultiple = getMatrixWithQuotient(quotients.get(currentIndex));
        System.out.println("First coeff matrix\n" + getStringRep(currentMultiple));
        quotients.forEach(i -> System.out.printf("%d ", i));
        System.out.println();
        while (currentIndex > 0) {
            System.out.printf("CurrentIndex: %d\n", currentIndex);
            currentIndex -= 1;
            System.out.println("*");
            RealMatrix currentMatrix = getMatrixWithQuotient(quotients.get(currentIndex));
            System.out.println(getStringRep(currentMatrix));
            System.out.println("=");
            currentMultiple = currentMultiple.multiply(currentMatrix);
            System.out.println(getStringRep(currentMultiple));

        }
        System.out.println("After all multiplications, the quotient matrix is: ");
        System.out.println(getStringRep(currentMultiple));

        long x = (long) currentMultiple.getRow(0)[0];
        long y = (long) currentMultiple.getRow(0)[1];

        System.out.printf("x = %d, y = %d, therefore %d * %d + %d * %d = %d\n",
                y, x, y, tmpM, x, tmpN, gcdValues.getLeft());
        return Pair.of(y, x);
    }

    public static Pair<BigInteger, BigInteger> findXYForExtendedEuclidean(BigInteger m, BigInteger n) {
        if (m.equals(ZERO)) {
            return Pair.of(n, ONE);
        }
        // form = n = q*m + r
        if (m.equals(n)) {
            return Pair.of(BigInteger.TWO, BigInteger.valueOf(-1L));
        }
        BigInteger tmpN = m.subtract(n).compareTo(ZERO) < 0 ? n : m;
        BigInteger tmpM = m.subtract(n).compareTo(ZERO) < 0 ? m : n;
//        System.out.printf("Starting Extended Euclidean, n = %s, m = %s\n", tmpN, tmpM);
        // assert that n > m
        assert (tmpN.compareTo(tmpM) > 0);
        Pair<BigInteger, List<BigInteger>> gcdValues = findGcdUsingEuclidean(tmpM, tmpN);
        List<BigInteger> quotients = gcdValues.getRight();
        int size = quotients.size();
//        printQuotientMatrixForLongs(quotients);
//        System.out.println("-------------------");
        int currentIndex = size - 1;
        BigInteger2x2Matrix currentMultiple = getMatrixWithQuotient(quotients.get(currentIndex));
//        System.out.println("First coeff matrix\n" + currentMultiple);
//        quotients.forEach(i -> System.out.printf("%d ", i));
//        System.out.println();
        while (currentIndex > 0) {
//            System.out.printf("CurrentIndex: %d\n", currentIndex);
            currentIndex -= 1;
            //System.out.println("*");
            BigInteger2x2Matrix currentMatrix = getMatrixWithQuotient(quotients.get(currentIndex));
            //System.out.println(currentMatrix);
            //System.out.println("=");
            currentMultiple = currentMultiple.multiply(currentMatrix);
            //System.out.println(currentMultiple);

        }
//        System.out.println("After all multiplications, the quotient matrix is: ");
//        System.out.println(currentMultiple);

        BigInteger x = currentMultiple.matrix[0][0];
        BigInteger y = currentMultiple.matrix[0][1];

//        System.out.printf("x = %d, y = %d, therefore %d * %d + %d * %d = %d\n",
//                y, x, y, tmpM, x, tmpN, gcdValues.getLeft());
        return Pair.of(y, x);
    }


    private static void printQuotientMatrix(List<Integer> quotients) {
        for (int i = quotients.size() -1; i >= 0; i--) {
            System.out.println(getStringRep(getMatrixWithQuotient(quotients.get(i))));
        }
    }

    private static void printQuotientMatrixForLongs(List<Long> quotients) {
        for (int i = quotients.size() -1; i >= 0; i--) {
            System.out.println(getStringRep(getMatrixWithQuotient(quotients.get(i))));
        }
    }

//    private static void printQuotientMatrixForBigIntegers(List<BigInteger> quotients) {
//        for (int i = quotients.size() -1; i >= 0; i--) {
//            System.out.println(getStringRep(getMatrixWithQuotient(quotients.get(i))));
//        }
//    }

    public static RealMatrix getMatrixWithQuotient(int quotient) {
        return new Array2DRowRealMatrix(
                new double[][]{
                        new double[]{0, 1},
                        new double[]{1, -1 * quotient}
                }
        );
    }

    public static RealMatrix getMatrixWithQuotient(long quotient) {
        return new Array2DRowRealMatrix(
                new double[][]{
                        new double[]{0, 1},
                        new double[]{1, -1 * quotient}
                }
        );
    }

    public static BigInteger2x2Matrix getMatrixWithQuotient(BigInteger quotient) {
        return new BigInteger2x2Matrix(
                new BigInteger[][]{
                        new BigInteger[]{ZERO, ONE},
                        new BigInteger[]{ONE, quotient.multiply(BigInteger.valueOf(-1))}
                }
        );
    }

    public static String getStringRep(RealMatrix matrix) {
        double[][] array = matrix.getData();

        final StringBuffer str = new StringBuffer();
        int i = 0, j = 0;
        for (i = 0; i < array.length; i++) {
            for (j = 0; j < array[i].length; j++) {
                str.append(" " + array[i][j]);
            }
            str.append("\n");
        }
        str.append("\n");
        return str.toString();
    }
}
