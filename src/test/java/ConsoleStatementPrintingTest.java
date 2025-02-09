import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsoleStatementPrintingTest {

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
    void givenEmptyOperations_whenPrintStatement_thenPrintStatementTitle() {
        ConsoleStatementPrinting statementPrinting = new ConsoleStatementPrinting();
        List<Operation> emptyOperations = new ArrayList<>();

        statementPrinting.printStatement(emptyOperations);

        assertEquals("   Date    || Amount || Balance\n", outputStreamCaptor.toString());
    }

    @Test
    void givenOperations_whenPrintStatement_thenPrintStatementTitleAndAllOperations() {
        ConsoleStatementPrinting statementPrinting = new ConsoleStatementPrinting();
        LocalDateTime dateTime = LocalDate.of(2025, Month.FEBRUARY, 1).atStartOfDay();
        List<Operation> operations = new ArrayList<>();
        operations.add(new Operation(dateTime, new Amount(100)));
        operations.add(new Operation(dateTime.plusDays(2), new Amount(2000)));
        operations.add(new Operation(dateTime.plusDays(3), new Amount(BigDecimal.valueOf(-300))));

        statementPrinting.printStatement(operations);

        assertEquals("   Date    || Amount || Balance\n" +
                        "04/02/2025 || -300.00 || 1 800.00\n" +
                        "03/02/2025 || 2 000.00 || 2 100.00\n" +
                        "01/02/2025 || 100.00 || 100.00\n"
                , outputStreamCaptor.toString());

    }


}
