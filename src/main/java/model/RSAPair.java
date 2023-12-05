package model;

import java.math.BigInteger;

import static java.lang.String.format;

public class RSAPair {
    private final BigInteger exponent;
    private final BigInteger modulus;


    public RSAPair(BigInteger exponent, BigInteger modulus) {
        this.exponent = exponent;
        this.modulus = modulus;
    }


    public BigInteger getModulus() {
        return modulus;
    }

    public BigInteger getExponent() {
        return exponent;
    }

    @Override
    public String toString() {
        return format("RSAPub: (modulus: %s, exponent: %s)", modulus, exponent);
    }
}
