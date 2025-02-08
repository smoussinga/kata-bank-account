import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OperationsTest {
    private final Operations operations = new Operations();

    @Test
    void givenInitialOperations_whenRetrieveAllOperations_thenShouldBeEmpty() {
        assertTrue(operations.retrieveAllOperations().isEmpty());
    }

    @Test
    void givenOperations_whenRecordOperationAndRetrieveAllOperations_thenReturnAllOperations() {
        var firstOperation = new Operation(LocalDateTime.now(), new Amount(100));
        var secondOperation = new Operation(LocalDateTime.now().plusHours(1), new Amount(200));
        var thirdOperation = new Operation(LocalDateTime.now().plusHours(2), new Amount(300));
        List<Operation> givenOperations = new ArrayList<>();
        givenOperations.add(firstOperation);
        givenOperations.add(secondOperation);
        givenOperations.add(thirdOperation);

        operations.recordOperation(firstOperation);
        operations.recordOperation(secondOperation);
        operations.recordOperation(thirdOperation);

        assertTrue(operations.retrieveAllOperations().containsAll(givenOperations));
    }

}
