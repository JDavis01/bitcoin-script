import java.util.Stack;

/**
 * Operation for hashing.
 * @author jacob davis
 * @version 6/9/19
 */
public class OpHash implements Operation {

    @Override
    public void execute(Stack<String> stack) throws VerificationException {
        String h = stack.pop();
        String hs = CryptoUtil.hash(h);
        stack.push(hs);
    }
}
