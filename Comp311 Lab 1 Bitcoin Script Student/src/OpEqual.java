import java.util.Stack;

/**
 * @author Jacob Davis
 * @version 5/30/19
 */
public class OpEqual implements Operation {

    @Override
    public void execute(Stack<String> stack) throws VerificationException {
        String first = stack.pop();
        String second = stack.pop();
        if (first.equals(second)) {
            stack.push("1");
        }
        else {
            stack.push("0");
        }
    }
}
