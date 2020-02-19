import java.security.PublicKey;
import java.util.Stack;
import java.util.ArrayList;

/**
 * Operation to check signatures.
 * @author jacob davis
 * @version 6/9/19
 */
public class OpMultiCheckSig implements Operation {
    /**
     * Data to check signatures.
     */
    private String transaction;

    /**
     * Constructor for OpMultiCheckSig.
     * @param transaction data to check sigs
     */
    public OpMultiCheckSig(String transaction) {
        this.transaction = transaction;
    }

    @Override
    public void execute(Stack<String> stack) throws VerificationException {
        String data = this.transaction;
        int trueCount = 0;
        int lowNum = 0;
        int numKey = Integer.parseInt(stack.pop());
        ArrayList<PublicKey> keys = new ArrayList<PublicKey>();
        for (int i = 0; i < numKey; i++) {
            keys.add(CryptoUtil.publicKeyFromString(stack.pop()));
        }
        int numSig = Integer.parseInt(stack.pop());
        String [] sigs = new String [numSig];
        for (int i = 0; i < numSig; i++) {
            sigs[i] = stack.pop();
        }
        if (numKey < numSig) {
            lowNum = numKey;
        }
        else {
            lowNum = numSig;
        }
        while (!keys.isEmpty()) {
        	for (int i = 0; i < lowNum; i++) {
        		if (CryptoUtil.checkSignature(
        				transaction, sigs[i], keys.get(i))) {
        			trueCount++;
        			keys.remove(i);
        		}
        	}
        }
        if (trueCount == numSig) {
            stack.push("1");
        }
        else {
            stack.push("0");
        }
    }
}
