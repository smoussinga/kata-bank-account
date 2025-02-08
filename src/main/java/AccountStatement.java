import java.time.LocalDateTime;

public record AccountStatement(Operation Operation, Amount balance) {

    public LocalDateTime getOperationDate(){
        return Operation.date();
    }
}
