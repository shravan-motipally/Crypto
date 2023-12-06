package model;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.math.BigInteger.*;

import static java.math.BigInteger.valueOf;
import static org.junit.jupiter.api.Assertions.*;

public class BigInteger2x2MatrixTest {

    @Test
    public void testMatrixMultiplication() {
        BigInteger2x2Matrix matrixOne = parseString("-2|3|-1|4", "\\|");
        BigInteger2x2Matrix matrixTwo = parseString("6|4|3|-1", "\\|");
        BigInteger2x2Matrix result = parseString("-3|-11|6|-8", "\\|");
        assertEquals(matrixOne.multiply(matrixTwo), result);
    }

    @Test
    public void testUtility() {
        BigInteger[][] matrix = new BigInteger[2][2];
        matrix[0][0] = valueOf(-2);
        matrix[0][1] = valueOf(3);
        matrix[1][0] = valueOf(-1);
        matrix[1][1] = valueOf(4);
        assertEquals(new BigInteger2x2Matrix(matrix), parseString("-2|3|-1|4", "\\|"));
    }

    private BigInteger2x2Matrix parseString(String str, String sep) {
        String[] nums = str.split(sep);
        assert(nums.length == 4);
        BigInteger[][] matrix = new BigInteger[2][2];
        matrix[0][0] = new BigInteger(nums[0]);
        matrix[0][1] = new BigInteger(nums[1]);
        matrix[1][0] = new BigInteger(nums[2]);
        matrix[1][1] = new BigInteger(nums[3]);
        return new BigInteger2x2Matrix(matrix);
    }
}