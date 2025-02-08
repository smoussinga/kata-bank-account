package exceptions;

public class MinimumAmountAllowedException extends OperationException {
    public MinimumAmountAllowedException() {
        super("The amount allowed for this operation should be different of 0");
    }
}
