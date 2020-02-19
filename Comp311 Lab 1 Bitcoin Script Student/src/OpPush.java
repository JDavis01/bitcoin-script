import java.util.Stack;

/**
 * Push operator.
 * @author jacob davis
 * @version 6/9/19
 */
public class OpPush implements Operation {
    /**
     * Argument for push.
     */
    private String arg;

    /**
     * Constructor for push.
     * @param arg to be pushed on stack
     */
    public OpPush(String arg) {
        this.arg = arg;
    }

    /**
     * Pushes argument on top of stack.
     * @param stack a stack of args
     * @throws VerificationException an exception
     */
    @Override
    public void execute(Stack<String> stack) throws VerificationException {
        stack.push(arg);
    }
}
