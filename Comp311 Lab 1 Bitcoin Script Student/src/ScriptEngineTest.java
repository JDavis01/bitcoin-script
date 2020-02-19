import java.security.KeyPair;
import java.util.Stack;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * @author jacob davis
 * @version 6/9/19
 */
public class ScriptEngineTest extends TestCase {
    /**
     * Engine to be used in testing.
     */
    private ScriptEngine sEngine;

    /**
     * Sets up script engine.
     * @throws Exception an exception
     */
    protected final void setUp() throws Exception {
        super.setUp();
        sEngine = new ScriptEngine(CryptoUtil.hash("123"));
    }

    /**
     * Tests push operation.
     * @throws VerificationException an exception
     */
    public final void testOpPush() throws VerificationException {
        assertTrue(sEngine.execute("OP_PUSH a\nOP_PUSH b"));
        Stack<String> s = new Stack<String>();
        s.push("a");
        s.push("b");
        assertEquals(s, sEngine.getStack());

        sEngine.rawExecute("OP_PUSH 123\nOP_PUSH Jake");
        s.push("123");
        s.push("Jake");
        assertEquals(s, sEngine.getStack());
    }

    /**
     * Tests verify operation.
     * @throws VerificationException an exception
     */
    @Test
    public final void testOpVerify() throws VerificationException {
        sEngine.rawExecute("OP_PUSH 1\nOP_PUSH 2\nOP_VERIFY");
        Stack<String> s = new Stack<String>();
        s.push("1");
        assertEquals(s, sEngine.getStack());

        sEngine.rawExecute("OP_PUSH 2\nOP_VERIFY\nOP_PUSH 3");
        s.push("3");
        assertEquals(s, sEngine.getStack());
    }

    /**
     * Tests equals operation.
     * @throws VerificationException an exception
     */
    public final void testOpEqual() throws VerificationException {
        sEngine.rawExecute("OP_PUSH abc\nOP_PUSH def\nOP_EQUAL");
        Stack<String> s = new Stack<String>();
        s.push("0");
        assertEquals(s, sEngine.getStack());

        sEngine.rawExecute("OP_PUSH abc\nOP_PUSH abc\nOP_EQUAL");
        s.push("1");
        assertEquals(s, sEngine.getStack());
    }

    /**
     * Test duplicate operation.
     * @throws VerificationException an exception
     */
    public final void testOpDup() throws VerificationException {
        sEngine.rawExecute("OP_PUSH abc\nOP_DUP");
        Stack<String> s = new Stack<String>();
        s.push("abc");
        s.push("abc");
        assertEquals(s, sEngine.getStack());
    }

    /**
     * Test hash operation.
     * @throws VerificationException an exception
     */
    public final void testOpHash() throws VerificationException {
        sEngine.rawExecute("OP_PUSH brown\nOP_HASH");
        String hs = CryptoUtil.hash("brown");
        String hb = sEngine.getStack().pop();
        assertEquals(hs, hb);
    }

    /**
     * Tests check sig operation.
     * @throws VerificationException an exception
     */
    public final void testCheckSig() throws VerificationException {
        KeyPair pair = CryptoUtil.generateKeyPair();
        String data = CryptoUtil.hash("123");
        String sig = CryptoUtil.signData(data, pair.getPrivate());
        String pKey = CryptoUtil.stringFromPublicKey(pair.getPublic());
        sEngine.rawExecute(String.format(
                "OP_PUSH %s\nOP_PUSH %s\nOP_CHECKSIG", sig, pKey));

        assertEquals("1", sEngine.getStack().peek());

        String badSig = CryptoUtil.signData("Jake", pair.getPrivate());
        sEngine.rawExecute(String.format(
                "OP_PUSH %s\nOP_PUSH %s\nOP_CHECKSIG", badSig, pKey));
        Stack<String> s = new Stack<String>();
        s.push("1");
        s.push("0");
        assertEquals(s, sEngine.getStack());
    }

    /**
     * Tests multi check sig operation.
     * @throws VerificationException an exception
     */
    public final void testMultiCheckSig() throws VerificationException {
        KeyPair pair = CryptoUtil.generateKeyPair();
        KeyPair pair2 = CryptoUtil.generateKeyPair();
        KeyPair pair3 = CryptoUtil.generateKeyPair();
        String data = CryptoUtil.hash("123");
        String sig = CryptoUtil.signData(data, pair.getPrivate());
        String sig2 = CryptoUtil.signData(data, pair2.getPrivate());
        String sig3 = CryptoUtil.signData(data, pair3.getPrivate());
        String pKey = CryptoUtil.stringFromPublicKey(pair.getPublic());
        String pKey2 = CryptoUtil.stringFromPublicKey(pair2.getPublic());
        String pKey3 = CryptoUtil.stringFromPublicKey(pair3.getPublic());
        sEngine.rawExecute(String.format("OP_PUSH %s\nOP_PUSH %s\nOP_PUSH %s\n"
                + "OP_PUSH 3\nOP_PUSH %s\nOP_PUSH %s\nOP_PUSH %s\nOP_PUSH 3\n"
                + "OP_CHECKMULTISIG", sig, sig2, sig3, pKey, pKey2, pKey3));

        assertEquals("1", sEngine.getStack().peek());

        ScriptEngine se2 = new ScriptEngine(CryptoUtil.hash("123"));
        se2.rawExecute(String.format("OP_PUSH %s\nOP_PUSH %s\n"
                + "OP_PUSH 2\nOP_PUSH %s\nOP_PUSH %s\nOP_PUSH %s\nOP_PUSH 3\n"
                + "OP_CHECKMULTISIG", sig, sig2, pKey, pKey2, pKey3));

        assertEquals("1", se2.getStack().peek());
    }
}
