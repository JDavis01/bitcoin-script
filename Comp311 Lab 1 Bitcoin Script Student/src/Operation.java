import java.util.Stack;

/**
 * 
 * @author jacob davis
 * @version 6/9/19
 */
public interface Operation {
    /**
     * 
     * @param stack a stack to execute
     * @throws VerificationException an exception
     */
    void execute(Stack<String> stack) throws VerificationException;
}
