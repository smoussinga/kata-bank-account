import java.util.List;

public interface OperationsRecord {

    void recordOperation(Operation operation);

    List<Operation> retrieveAllOperations();

}
