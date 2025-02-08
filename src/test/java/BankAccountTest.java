import exceptions.MinimumAmountAllowedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BankAccountTest {

    @Mock
    OperationsRecord operationsRecord;

    @Mock
    StatementPrinting statementPrinting;

    @Test
    public void givenAmount_whenDeposit_thenRecordOperation() throws MinimumAmountAllowedException {
        BankAccount bankAccount = new BankAccount(operationsRecord, statementPrinting);
        LocalDateTime currentDate = LocalDateTime.now();
        var amount = new Amount(500);

        try (MockedStatic<LocalDateTime> dateTimeMockedStatic = Mockito.mockStatic(LocalDateTime.class)) {
            dateTimeMockedStatic.when(LocalDateTime::now).thenReturn(currentDate);
            assertTrue(LocalDateTime.now().isEqual(currentDate));

            bankAccount.deposit(amount);
            var depositOperation = new Operation(LocalDateTime.now(), amount);
            verify(operationsRecord).recordOperation(depositOperation);
        }
    }

    @Test
    void givenAmountZero_whenMinimumAmountAllowedExceptionThrown_thenAssertionSucceeds() {
        BankAccount bankAccount = new BankAccount(operationsRecord, statementPrinting);
        var amount = new Amount(0);

        Exception exception = assertThrows(MinimumAmountAllowedException.class, () -> bankAccount.deposit(amount));

        String expectedMessage = "The amount allowed for this operation should be different of 0";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
