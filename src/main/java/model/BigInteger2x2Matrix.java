package model;

import java.math.BigInteger;

public class BigInteger2x2Matrix {

    public final BigInteger[][] matrix;


    public BigInteger2x2Matrix(BigInteger[][] matrix) {
        assert(matrix.length == 2);
        assert(matrix[0].length == 2);
        assert(matrix[1].length == 2);
        this.matrix = matrix;
    }

    /**
     * (a b)(i ii)    = (a * i + b*iii)  (a*ii + b*iv)
     * (c d)(iii iv)    (c * i + d*iii)  (c*ii +d *iv)
     * @param other
     * @return
     */

    public BigInteger2x2Matrix multiply(BigInteger2x2Matrix other) {
        BigInteger[][] result = new BigInteger[2][2];
        result[0][0] = matrix[0][0].multiply(other.matrix[0][0]).add(matrix[0][1].multiply(other.matrix[1][0]));
        result[0][1] = matrix[0][0].multiply(other.matrix[0][1]).add(matrix[0][1].multiply(other.matrix[1][1]));
        result[1][0] = matrix[1][0].multiply(other.matrix[0][0]).add(matrix[1][1].multiply(other.matrix[1][0]));
        result[1][1] = matrix[1][0].multiply(other.matrix[0][1]).add(matrix[1][1].multiply(other.matrix[1][1]));
        return new BigInteger2x2Matrix(result);
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                str.append(" " + matrix[i][j]);
            }
            str.append("\n");
        }
        str.append("\n");
        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        BigInteger2x2Matrix oth = (BigInteger2x2Matrix) o;
        BigInteger[][] other = oth.matrix;
        return matrix[0][0].equals(other[0][0]) &&
                matrix[0][1].equals(other[0][1]) &&
                matrix[1][0].equals(other[1][0]) &&
                matrix[1][1].equals(other[1][1]);
    }

    @Override
    public int hashCode() {
        BigInteger mod = BigInteger.valueOf(Integer.MAX_VALUE);
        return matrix[0][0].add(BigInteger.TWO.multiply(matrix[0][1]))
                .add(BigInteger.valueOf(3).multiply(matrix[1][0]))
                .add(BigInteger.valueOf(4).multiply(matrix[1][1]))
                .mod(mod).intValue();
    }
}
