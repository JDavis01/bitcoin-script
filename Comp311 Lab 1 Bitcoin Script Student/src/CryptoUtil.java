import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

/**
 * Crypto utilities.
 * 
 * @author tim
 * @version 1.0
 *
 */
public class CryptoUtil {

    /**
     * Fake key implementation because WebCAT had issues with real EC keys.
     * 
     * @author tim
     * @version 1.0
     *
     */
    @SuppressWarnings("serial")
    private static class FakeKey implements PublicKey, PrivateKey {
        /**
         * A key.
         */
        private byte[] key;

        /**
         * Create a FakeKey.
         */
        public FakeKey() {
            this.key = new byte[16];
            new Random().nextBytes(key);
        }

        /**
        * Create a FakeKey from a byte array.
        * 
        * @param key the byte array
        */
        public FakeKey(final byte[] key) {
            this.key = key;
        }

        @Override
        public String getAlgorithm() {
            return "FakeKey";
        }

        @Override
        public byte[] getEncoded() {
            return key;
        }

        @Override
        public String getFormat() {
            return "Some format";
        }

        @Override
        public boolean equals(final Object obj) {
            FakeKey that = (FakeKey) obj;
            return Arrays.equals(this.key, that.key);
        }
    }

    /**
     * Returns a string of hex digits representing an array of bytes.
     * 
     * @param data the bytes
     * @return a string of hex digits representing an array of bytes
     */
    public static String stringFromBytes(final byte[] data) {
        return DatatypeConverter.printHexBinary(data).toLowerCase();
    }

    /**
     * Returns an array of bytes created from a string of hex digits.
     * 
     * @param hexString the string
     * @return an array of bytes created from a string of hex digits
     */
    public static byte[] byteArrayFromHexString(final String hexString) {
        return DatatypeConverter.parseHexBinary(hexString);
    }

    /**
     * Converts a public key to a string.
     * 
     * @param key the key
     * @return the string
     */
    public static String stringFromPublicKey(final PublicKey key) {
        return stringFromBytes(key.getEncoded());
    }

    /**
     * Converts a string into a public key.
     * 
     * @param keyStr the key as a string
     * @return the key
     */
    public static PublicKey publicKeyFromString(final String keyStr) {
        return new FakeKey(byteArrayFromHexString(keyStr));
    }

    /**
     * Get a message digest.
     * 
     * @param alg the algorithm
     * @return the digest
     */
    public static MessageDigest getDigest(final String alg) {
        try {
            return MessageDigest.getInstance(alg);
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * Hashes a string with SHA-256.
     * 
     * @param data the string
     * @return the hash
     */
    public static String hash(final String data) {
        MessageDigest md = getDigest("SHA-256");
        byte[] digest = md.digest(data.getBytes());
        return stringFromBytes(digest);
    }

    /**
     * Generates a public-private key pair.
     * 
     * @return the pair
     */
    public static KeyPair generateKeyPair() {
        FakeKey key = new FakeKey();
        return new KeyPair(key, key);
    }

    /**
     * Signs the given data with the supplied private key.
     * 
     * @param data       the data
     * @param privateKey the private key
     * @return the signature
     */
    public static String signData(
            final String data, final PrivateKey privateKey) {
        MessageDigest md5 = getDigest("MD5");
        md5.update(data.getBytes());
        md5.update(privateKey.getEncoded());
        return stringFromBytes(md5.digest());
    }

    /**
     * Checks whether signature was computed 
     * using the private key that matches the
     * supplied public key.
     * 
     * @param data      the data that was signed
     * @param signature the signature
     * @param publicKey the public key
     * @return true if valid
     */
    public static boolean checkSignature(
            final String data, final String signature, 
            final PublicKey publicKey) {
        MessageDigest md5 = getDigest("MD5");
        md5.update(data.getBytes());
        md5.update(publicKey.getEncoded());
        return signature.equals(stringFromBytes(md5.digest()));
    }
}
