package exceptions;

public class InsufficientFundsForWithdrawalException extends OperationException {
    public InsufficientFundsForWithdrawalException() {
        super("The balance fund is insufficient for the withdrawal amount");
    }
}
