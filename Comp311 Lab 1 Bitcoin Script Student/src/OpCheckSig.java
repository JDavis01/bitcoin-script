import java.security.PublicKey;
import java.util.Stack;

/**
 * Operation to check signature.
 * @author jacob davis
 * @version 6/9/19
 */
public class OpCheckSig implements Operation {
    /*
     * Transaction data
     */
    private String transaction;

    /**
     * Constructor for OpCheckSig.
     * @param transaction data to check sig
     */
    public OpCheckSig(String transaction) {
        this.transaction = transaction;
    }

    @Override
    public void execute(Stack<String> stack) throws VerificationException {
        String data = this.transaction;
        String pKey = stack.pop();
        String sig = stack.pop();
        PublicKey pubKey = CryptoUtil.publicKeyFromString(pKey);

        if (CryptoUtil.checkSignature(data, sig, pubKey)) {
            stack.push("1");
        }
        else {
            stack.push("0");
        }
    }
}
