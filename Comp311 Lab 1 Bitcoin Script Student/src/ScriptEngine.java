import java.util.Stack;
/**
 * Script Engine.
 * 
 * @author jacob davis
 * @version 5/26/19
 */
public class ScriptEngine {
    /**
     * Transaction data.
     */
    private static String transactionHash;
    /**
     * Stack of commands.
     */
    private Stack<String> opStack;

    /**
     * Constructor for Script Engine.
     * @param transactionHash data to complete transaction
     */
    public ScriptEngine(String transactionHash) {
        this.transactionHash = transactionHash;
        opStack = new Stack<String>();
    }

    /**
     * 
     * @return a stack
     */
    public Stack<String> getStack() {
        return opStack;
    }

    /**
     * 
     * @param op
     * @return an Operator
     */
    private static Operation getOperator(String op) {
        if (op.contains("OP_PUSH")) {
            String[] value = op.split(" ");
            OpPush push = new OpPush(value[1]);
            return push;
        }
        else if (op.contains("OP_VERIFY")) {
            OpVerify verify = new OpVerify();
            return verify;
        }
        else if (op.contains("OP_EQUAL")) {
            OpEqual equal = new OpEqual();
            return equal;
        }
        else if (op.contains("OP_DUP")) {
            OpDup dup = new OpDup();
            return dup;
        }
        else if (op.contains("OP_HASH")) {
            OpHash hash = new OpHash();
            return hash;
        }
        else if (op.contains("OP_CHECKSIG")) {
            OpCheckSig check = new OpCheckSig(transactionHash);
            return check;
        }
        else if (op.contains("OP_CHECKMULTISIG")) {
            OpMultiCheckSig mCheck = new OpMultiCheckSig(transactionHash);
            return mCheck;
        }
        else {
            return null;
        }
    }

    /**
     * 
     * @param script
     * @param script a string to execute
     * @throws VerificationException an exception
     */
    public void rawExecute(String script) throws VerificationException {
        String[] ops = script.split("\n");
        for (int i = 0; i < ops.length; i++) {
            String op = ops[i].toString();
            Operation operator = ScriptEngine.getOperator(op);
            operator.execute(opStack);
        }
    }

    /**
     * Returns true if script executes successfully.
     * @param script a string of instructions
     * @return true if successful
     */
    public boolean execute(String script) {
        try {
            rawExecute(script);
        } 
        catch (VerificationException e) {
            e.printStackTrace();
        }
        return !opStack.peek().equals("0");
    }
}
