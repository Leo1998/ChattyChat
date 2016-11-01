package crypto;

import java.math.BigInteger;

public class Crypto {

    public static String encrypt(String original, KeyPair.PublicKey publicKey) {
        char[] chars = original.toCharArray();
        BigInteger[] encrypted = new BigInteger[chars.length];

        for (int i = 0; i < chars.length; i++) {
            BigInteger m = new BigInteger(Integer.toString(chars[i]));

            BigInteger c = m.modPow(publicKey.e, publicKey.n);

            encrypted[i] = c;
        }

        return encode(encrypted);
    }

    public static String decrypt(String encryptedString, KeyPair.PrivateKey privateKey) {
        BigInteger[] encrypted = decode(encryptedString);
        char[] original = new char[encrypted.length];

        for (int i = 0; i < encrypted.length; i++) {
            BigInteger c = encrypted[i];

            BigInteger m = c.modPow(privateKey.d, privateKey.n);

            original[i] = (char) m.intValueExact();
        }

        return new String(original);
    }

    public static String encode(BigInteger bigInt) {
        byte[] data = bigInt.toByteArray();

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < data.length; ++i) {
            buffer.append(Integer.toHexString(0x0100 + (data[i] & 0x00FF)).substring(1));
        }

        return buffer.toString();
    }

    public static String encode(BigInteger[] bigInts) {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < bigInts.length; i++) {
            buffer.append(encode(bigInts[i]));

            if (i < bigInts.length - 1) {
                buffer.append(",");
            }
        }

        return buffer.toString();
    }

    public static BigInteger[] decode(String s) {
        String[] tokens = s.split(",");
        BigInteger[] result = new BigInteger[tokens.length];

        for (int i = 0; i < tokens.length; i++) {
            result[i] = new BigInteger(tokens[i], 16);
        }

        return result;
    }

}
