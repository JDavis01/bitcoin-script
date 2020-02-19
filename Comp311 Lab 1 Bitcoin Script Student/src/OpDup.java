import java.util.Stack;

/**
 * Operation to duplicate.
 * @author jacob davis
 * @version 6/9/19
 */
public class OpDup implements Operation {

    @Override
    public void execute(Stack<String> stack) throws VerificationException {
        String copy = stack.peek();
        stack.push(copy);
    }
}
