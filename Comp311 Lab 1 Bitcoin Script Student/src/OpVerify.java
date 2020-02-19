import java.util.Stack;

/**
 * 
 * @author jacob davis
 * @version 6/9/19
 */
public class OpVerify implements Operation {
    @Override
    public void execute(Stack<String> stack) throws VerificationException {
        String value = stack.pop();
        if (value.equals("0")) {
            throw new VerificationException();
        }
    }
}
