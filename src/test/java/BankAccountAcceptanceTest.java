import exceptions.MinimumAmountAllowedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

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

        bankAccount.deposit(Amount.toAmount(2000));
        bankAccount.deposit(Amount.toAmount(BigDecimal.valueOf(1000.00)));
        bankAccount.deposit(Amount.toAmount(BigDecimal.valueOf(500.50)));
        bankAccount.deposit(Amount.toAmount(BigDecimal.valueOf(1500.50)));

        bankAccount.printStatement();

        LocalDateTime dateTime = LocalDate.of(2025, Month.FEBRUARY, 1).atStartOfDay();
        List<Operation> operations = new ArrayList<>();
        operations.add(new Operation(dateTime, new Amount(2000)));
        operations.add(new Operation(dateTime.plusDays(2), new Amount(1000)));
        operations.add(new Operation(dateTime.plusDays(3), new Amount(BigDecimal.valueOf(500.50))));
        operations.add(new Operation(dateTime.plusDays(4), new Amount(BigDecimal.valueOf(1500.50))));

        verify(statementPrinting).printStatement(operations);

    }

}
