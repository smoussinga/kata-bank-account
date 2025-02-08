import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Operation(LocalDateTime date, BigDecimal amount) {
}
