import exceptions.InsufficientFundsForWithdrawalException;
import exceptions.MinimumAmountAllowedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BankAccountAcceptanceTest {

    @Mock
    OperationsRecord operationsRecord;

    @Mock
    StatementPrinting statementPrinting;

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }


    @Test
    void givenOperations_whenPrintStatement_thenPrintStatementTitleAndAllOperations() throws MinimumAmountAllowedException, InsufficientFundsForWithdrawalException {
        Operations operationsRecord = new Operations();
        ConsoleStatementPrinting statementPrinting = new ConsoleStatementPrinting();
        LocalDateTime dateTime = LocalDate.of(2025, Month.FEBRUARY, 8).atStartOfDay();

        try (MockedStatic<LocalDateTime> dateTimeMockedStatic = Mockito.mockStatic(LocalDateTime.class)) {
            dateTimeMockedStatic.when(LocalDateTime::now)
                    .thenReturn(dateTime)
                    .thenReturn(dateTime.plusDays(2))
                    .thenReturn(dateTime.plusDays(3))
                    .thenReturn(dateTime.plusDays(4))
                    .thenReturn(dateTime.plusDays(5));

            BankAccount bankAccount = new BankAccount(operationsRecord, statementPrinting);
            bankAccount.deposit(new Amount(500));
            bankAccount.deposit(new Amount(2000));
            bankAccount.withdraw(new Amount(BigDecimal.valueOf(-2400)));
            bankAccount.deposit(new Amount(3900));
            bankAccount.withdraw(new Amount(3999));

            bankAccount.printStatement();

            assertEquals("   Date    || Amount || Balance\n" +
                            "13/02/2025 || -3 999.00 || 1.00\n" +
                            "12/02/2025 || 3 900.00 || 4 000.00\n" +
                            "11/02/2025 || -2 400.00 || 100.00\n" +
                            "10/02/2025 || 2 000.00 || 2 500.00\n" +
                            "08/02/2025 || 500.00 || 500.00\n"
                    , outputStreamCaptor.toString());

        }
    }

}
