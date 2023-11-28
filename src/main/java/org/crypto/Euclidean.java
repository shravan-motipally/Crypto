package org.crypto;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.List;

import static org.crypto.Utils.DOUBLE_TAB;

public class Euclidean {
    private static final String EUCLIDEAN_TEMPLATE = "%d" + DOUBLE_TAB + "="
            + DOUBLE_TAB + "%d" + DOUBLE_TAB + "*"
            + DOUBLE_TAB + "%d" + DOUBLE_TAB +
            "+" + DOUBLE_TAB + "%d\n";

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
        if (tmpN == 1) {
            System.out.printf("Note: %d and %d are relatively prime.\n", m, n);
        }
        return Pair.of(tmpN, quotients);
    }

    public static Pair<Long, List<Long>> findGcdUsingEuclidean(long m, long n) {
        // form = n = q*m + r
        if (m == n) {
            return Pair.of(m, new ArrayList<>() {{
                add(1L);
            }});
        }
        long tmpN = Math.max(m, n);
        long tmpM = Math.min(m, n);
        System.out.printf("Starting Euclidean, n = %d, m = %d\n", tmpN, tmpM);
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
            System.out.printf(EUCLIDEAN_TEMPLATE, tmpN, q, tmpM, r);
            tmpN = tmpM;
            tmpM = r;
        } while (r != 0);

        // at this point tmpN has last value of r.
        System.out.printf("GCD(%d, %d) = %d.\n", m, n, tmpN);
        if (tmpN == 1) {
            System.out.printf("Note: %d and %d are relatively prime.\n", m, n);
        }
        return Pair.of(tmpN, quotients);
    }

    public static Pair<Integer, Integer> findXYForExtendedEuclidean(int m, int n) {
        // form = n = q*m + r
        if (m == n) {
            return Pair.of(2, -1);
        }
        int tmpN = Math.max(m, n);
        int tmpM = Math.min(m, n);
        System.out.printf("Starting Extended Euclidean, n = %d, m = %d\n", tmpN, tmpM);
        // assert that n > m
        assert (tmpN > tmpM);
        Pair<Integer, List<Integer>> gcdValues = findGcdUsingEuclidean(tmpM, tmpN);
        List<Integer> quotients = gcdValues.getRight();
        int size = quotients.size();
        printQuotientMatrix(quotients);
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

        int x = (int) currentMultiple.getRow(0)[0];
        int y = (int) currentMultiple.getRow(0)[1];

        System.out.printf("x = %d, y = %d, therefore %d * %d + %d * %d = %d\n",
                y, x, y, tmpM, x, tmpN, gcdValues.getLeft());
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

    // TODO
    //public static int findMultiplicativeInverse()
}
