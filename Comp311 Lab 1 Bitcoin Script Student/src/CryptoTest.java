import java.security.KeyPair;
import java.security.PublicKey;

import junit.framework.TestCase;

/**
 * Tests for crypto utilities.
 * 
 * @author tim
 * @version 1.0
 *
 */
public class CryptoTest extends TestCase {
    /**
     * Test hashing.
     */
    public void testHashing() {
        assertEquals(
            "ba7816bf8f01cfea414140de5dae2223" 
            + "b00361a396177a9cb410ff61f20015ad", 
            CryptoUtil.hash("abc"));
    }

    /**
     * Test converting a key to a String and back.
     */
    public void testKeySerialization() {
    // Generate a key pair
        KeyPair pair = CryptoUtil.generateKeyPair();

    // Convert public key to a String
        String pubKeyStr = CryptoUtil.stringFromPublicKey(pair.getPublic());

    // Convert String back into a public key
        PublicKey pubKey = CryptoUtil.publicKeyFromString(pubKeyStr);

        assertEquals(pair.getPublic(), pubKey);
    }

    /**
     * Test signing and signature validation.
     */
    public void testSigning() {
    // Create a key pair
        KeyPair pair = CryptoUtil.generateKeyPair();

// Get the byte [] representing "Hi there", and convert to a hex string
// This will be "4869207468657265"
        String data = CryptoUtil.stringFromBytes("Hi there".getBytes());

    // Sign the data with the private key
        String sig = CryptoUtil.signData(data, pair.getPrivate());

    // Verify the signature with the public key
        assertTrue(CryptoUtil.checkSignature(data, sig, pair.getPublic()));

    // Use a signature for the wrong data, and check that validation fails
        String badsig = CryptoUtil.signData("Other msg", pair.getPrivate());
        assertFalse(CryptoUtil.checkSignature(data, badsig, pair.getPublic()));
    }
    /**
     * Tests for coverage.
     */
    public void testCoverage() {
        new CryptoUtil();
        KeyPair pair = CryptoUtil.generateKeyPair();
        assertEquals("FakeKey", pair.getPublic().getAlgorithm());
        assertEquals("Some format", pair.getPublic().getFormat());
        assertNull(CryptoUtil.getDigest("Not a real algorithm"));
    }
}
