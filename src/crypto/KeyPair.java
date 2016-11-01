package crypto;

import java.math.BigInteger;

public class KeyPair {

    public static class PublicKey {
        public final BigInteger n;
        public final BigInteger e;

        public PublicKey(BigInteger n, BigInteger e) {
            this.n = n;
            this.e = e;
        }

        @Override
        public String toString() {
            return "PublicKey{" +
                    "n=" + n +
                    ", e=" + e +
                    '}';
        }
    }

    public static class PrivateKey {
        public final BigInteger n;
        public final BigInteger d;

        public PrivateKey(BigInteger n, BigInteger d) {
            this.n = n;
            this.d = d;
        }

        @Override
        public String toString() {
            return "PrivateKey{" +
                    "n=" + n +
                    ", d=" + d +
                    '}';
        }
    }

    public final PublicKey publicKey;
    public final PrivateKey privateKey;

    KeyPair(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Override
    public String toString() {
        return "KeyPair{" +
                "publicKey=" + publicKey +
                ", privateKey=" + privateKey +
                '}';
    }
}
