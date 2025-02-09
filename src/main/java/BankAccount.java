import exceptions.MinimumAmountAllowedException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BankAccount implements Account {

    private final OperationsRecord operationsRecord;
    private final StatementPrinting statementPrinting;

    public BankAccount(OperationsRecord operationsRecord, StatementPrinting statementPrinting) {
        this.operationsRecord = operationsRecord;
        this.statementPrinting = statementPrinting;
    }

    @Override
    public void deposit(Amount amount) throws MinimumAmountAllowedException {
        if(isNotValidMinimumAmount(amount)){
            throw new MinimumAmountAllowedException();
        }
        recordOperation(amount);
    }

    @Override
    public void withdraw(Amount amount) throws MinimumAmountAllowedException {
        Amount negativeAmount = negateAmount(amount);
        if(isNotValidMinimumAmount(negativeAmount)){
            throw new MinimumAmountAllowedException();
        }
        recordOperation(negativeAmount);
    }

    @Override
    public void printStatement() {
        statementPrinting.printStatement(operationsRecord.retrieveAllOperations());
    }

    private boolean isNotValidMinimumAmount(Amount amount){
        return amount.getAmountValue().abs().compareTo(BigDecimal.ZERO) == 0;
    }

    private Amount negateAmount(Amount amount){
        BigDecimal amountValue = amount.getAmountValue();
        if(amountValue.signum() < 0){
            return amount;
        }
        if(amountValue.signum() > 0){
            return new Amount(amountValue.negate());
        }
        return amount;
    }

    private void recordOperation(Amount amount) {
        var operation = new Operation(LocalDateTime.now(), amount);
        operationsRecord.recordOperation(operation);
    }

}
