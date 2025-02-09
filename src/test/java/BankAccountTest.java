import exceptions.InsufficientFundsForWithdrawalException;
import exceptions.MinimumAmountAllowedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BankAccountTest {

    @Mock
    OperationsRecord operationsRecord;

    @Mock
    StatementPrinting statementPrinting;

    private final Operations operationsForWithdrawal = new Operations();
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();


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

    @Test
    public void givenOperations_whenWithdrawal_thenRecordOperation() throws MinimumAmountAllowedException, InsufficientFundsForWithdrawalException {
        LocalDateTime currentDate = LocalDateTime.now();
        var firstOperation = new Operation(currentDate, new Amount(1000));
        var secondOperation = new Operation(currentDate.plusHours(1), new Amount(2000));
        var thirdOperation = new Operation(currentDate.plusHours(2), new Amount(500));
        var fourthOperation = new Operation(currentDate.plusHours(3), new Amount(-200));
        List<Operation> givenOperations = new ArrayList<>();
        givenOperations.add(firstOperation);
        givenOperations.add(secondOperation);
        givenOperations.add(thirdOperation);
        givenOperations.add(fourthOperation);

        operationsForWithdrawal.recordOperation(firstOperation);
        operationsForWithdrawal.recordOperation(secondOperation);
        operationsForWithdrawal.recordOperation(thirdOperation);

        BankAccount bankAccount = new BankAccount(operationsForWithdrawal, statementPrinting);


        try (MockedStatic<LocalDateTime> dateTimeMockedStatic = Mockito.mockStatic(LocalDateTime.class)) {
            dateTimeMockedStatic.when(LocalDateTime::now).thenReturn(currentDate.plusHours(3));

            bankAccount.withdraw(new Amount(-200));

            BigDecimal firstWithdrawalAmountValue = operationsForWithdrawal.retrieveAllOperations().get(3).amount().getAmountValue();
            BigDecimal fourthOperationAmountValue = givenOperations.get(3).amount().getAmountValue();
            assertEquals(BigDecimal.valueOf(-200), firstWithdrawalAmountValue);
            assertEquals(0, firstWithdrawalAmountValue.compareTo(fourthOperationAmountValue));
        }
    }

    @Test
    void givenAmountGreaterThanBalance_whenInsufficientFundsForWithdrawalExceptionThrown_thenAssertionSucceeds() {
        Operations givenOperationsRecord = new Operations();
        LocalDateTime currentDate = LocalDateTime.now();
        var firstOperation = new Operation(currentDate, new Amount(1000));
        givenOperationsRecord.recordOperation(firstOperation);
        BankAccount bankAccount = new BankAccount(givenOperationsRecord, statementPrinting);
        var withdrawalAmount = new Amount(BigDecimal.valueOf(1000.01));

        Exception exception = assertThrows(InsufficientFundsForWithdrawalException.class, () -> bankAccount.withdraw(withdrawalAmount));

        String expectedMessage = "The balance fund is insufficient for the withdrawal amount";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenOperations_whenPrintStatement_thenPrintStatementTitleAndAllOperations() {
        System.setOut(new PrintStream(outputStreamCaptor));
        BankAccount bankAccount = getBankAccount();
        bankAccount.printStatement();

        assertEquals("   Date    || Amount || Balance\n" +
                        "11/02/2025 || -2 400.00 || 100.00\n" +
                        "10/02/2025 || 2 000.00 || 2 500.00\n" +
                        "08/02/2025 || 500.00 || 500.00\n"
                , outputStreamCaptor.toString());

        System.setOut(standardOut);

    }

    private static BankAccount getBankAccount() {
        ConsoleStatementPrinting statementPrinting = new ConsoleStatementPrinting();
        LocalDateTime dateTime = LocalDate.of(2025, Month.FEBRUARY, 8).atStartOfDay();
        Operations givenOperationsRecord = new Operations();
        givenOperationsRecord.recordOperation(new Operation(dateTime, new Amount(500)));
        givenOperationsRecord.recordOperation(new Operation(dateTime.plusDays(2), new Amount(2000)));
        givenOperationsRecord.recordOperation(new Operation(dateTime.plusDays(3), new Amount(BigDecimal.valueOf(-2400))));

        return new BankAccount(givenOperationsRecord, statementPrinting);
    }
}
