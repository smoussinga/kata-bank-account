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
    public void deposit(BigDecimal amount) throws MinimumAmountAllowedException {
        if(isNotValidMinimumAmount(amount)){
            throw new MinimumAmountAllowedException();
        }
        recordOperation(amount);
    }

    @Override
    public void printStatement() {
    }

    private boolean isNotValidMinimumAmount(BigDecimal amount){
        return amount.abs().compareTo(BigDecimal.ZERO) == 0;
    }

    private void recordOperation(BigDecimal amount) {
        var operation = new Operation(LocalDateTime.now(), amount);
        operationsRecord.recordOperation(operation);
    }

}
