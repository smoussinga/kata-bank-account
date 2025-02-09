import exceptions.MinimumAmountAllowedException;

public interface Account {

    void deposit(Amount amount) throws MinimumAmountAllowedException;
    void withdraw(Amount amount) throws MinimumAmountAllowedException;
    void printStatement();

}
