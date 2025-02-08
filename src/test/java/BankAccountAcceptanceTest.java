import exceptions.MinimumAmountAllowedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BankAccountAcceptanceTest {

    @Mock
    OperationsRecord operationsRecord;

    @Mock
    StatementPrinting statementPrinting;

    @Test
    public void should_print_a_statement_with_all_withdrawal_operations() throws MinimumAmountAllowedException {
        Account bankAccount = new BankAccount(operationsRecord, statementPrinting);

        bankAccount.deposit(BigDecimal.valueOf(2000));
        bankAccount.deposit(BigDecimal.valueOf(1000.00));
        bankAccount.deposit(BigDecimal.valueOf(500.50));
        bankAccount.deposit(BigDecimal.valueOf(1500.50));

        bankAccount.printStatement();

        String statements = "   Date    ||  Amount || Balance\n" +
                "05/02/2025 || 1500.50 || 5001.00\n" +
                "04/02/2025 || 500.50  || 3500.50\n" +
                "03/02/2025 || 1000.00 || 3000.00\n" +
                "01/02/2025 || 2000.00 || 2000.00\n";

        verify(statementPrinting).printStatement(statements);

    }

}
