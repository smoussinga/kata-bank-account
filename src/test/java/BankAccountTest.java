import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BankAccountTest {

    @Mock
    OperationsRecord operationsRecord;

    @Mock
    StatementPrinting statementPrinting;

    @Test
    public void givenAmount_whenDeposit_thenRecordTransaction() {
        BankAccount bankAccount = new BankAccount(operationsRecord, statementPrinting);
        LocalDateTime currentDate = LocalDateTime.now();
        var amount = new BigDecimal("500.00");

        try (MockedStatic<LocalDateTime> dateTimeMockedStatic = Mockito.mockStatic(LocalDateTime.class)) {
            dateTimeMockedStatic.when(LocalDateTime::now).thenReturn(currentDate);
            assertTrue(LocalDateTime.now().isEqual(currentDate));

            bankAccount.deposit(amount);
            var depositOperation = new Operation(LocalDateTime.now(), amount);
            verify(operationsRecord).recordOperation(depositOperation);
        }
    }
}
