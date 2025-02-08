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
    public void deposit(BigDecimal amount) {
        recordOperation(amount);
    }

    @Override
    public void printStatement() {
    }

    private void recordOperation(BigDecimal amount) {
        var operation = new Operation(LocalDateTime.now(), amount);
        operationsRecord.recordOperation(operation);
    }

}
