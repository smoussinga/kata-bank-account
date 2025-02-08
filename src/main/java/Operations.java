import java.util.ArrayList;
import java.util.List;

public class Operations implements OperationsRecord{

    private final List<Operation> operations = new ArrayList<>();

    @Override
    public void recordOperation(Operation operation) {
        operations.add(operation);
    }

    @Override
    public List<Operation> retrieveAllOperations() {
        return operations;
    }
}
