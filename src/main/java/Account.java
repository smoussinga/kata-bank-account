import exceptions.MinimumAmountAllowedException;

import java.math.BigDecimal;

public interface Account {

    void deposit(Amount amount) throws MinimumAmountAllowedException;
    void printStatement();

}
