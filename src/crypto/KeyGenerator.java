package crypto;

import java.math.BigInteger;
import java.util.Random;

public class KeyGenerator {

    public static KeyPair generateKeyPair(int bits) {
        return generateKeyPair(generatePrime(bits), generatePrime(bits));
    }

    private static KeyPair generateKeyPair(BigInteger p, BigInteger q) {
        BigInteger n = p.multiply(q);
        BigInteger r = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        BigInteger e = BigInteger.valueOf(2);
        while(isDivideable(r, e)) {
            e = e.add(BigInteger.ONE);
        }

        BigInteger d = e.modInverse(r);

        //System.out.println("N=" + n + ", e=" + e + ", d=" + d);

        KeyPair.PublicKey publicKey = new KeyPair.PublicKey(n, e);
        KeyPair.PrivateKey privateKey = new KeyPair.PrivateKey(n, d);

        return new KeyPair(publicKey, privateKey);
    }

    private static boolean isDivideable(BigInteger num, BigInteger divisor) {
        return num.mod(divisor).equals(BigInteger.ZERO);
    }

    private static BigInteger generatePrime(int bits){
        BigInteger prime = new BigInteger(bits, 999999, new Random());

        return prime;
    }

}
