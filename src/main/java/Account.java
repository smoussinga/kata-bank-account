import exceptions.MinimumAmountAllowedException;

import java.math.BigDecimal;

public interface Account {

    void deposit(BigDecimal amount) throws MinimumAmountAllowedException;
    void printStatement();

}
