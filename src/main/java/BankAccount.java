import java.math.BigDecimal;

public class BankAccount implements Account {

    private final StatementPrinting statementPrinting;

    public BankAccount(StatementPrinting statementPrinting) {
        this.statementPrinting = statementPrinting;
    }

    @Override
    public void deposit(BigDecimal amount) {;
    }

    @Override
    public void printStatement() {
    }
}
