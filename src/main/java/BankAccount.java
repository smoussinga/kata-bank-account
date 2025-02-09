import exceptions.InsufficientFundsForWithdrawalException;
import exceptions.MinimumAmountAllowedException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class BankAccount implements Account {

    private final OperationsRecord operationsRecord;
    private final StatementPrinting statementPrinting;

    public BankAccount(OperationsRecord operationsRecord, StatementPrinting statementPrinting) {
        this.operationsRecord = operationsRecord;
        this.statementPrinting = statementPrinting;
    }

    @Override
    public void deposit(Amount amount) throws MinimumAmountAllowedException {
        if(isNotValidMinimumAmount(amount)){
            throw new MinimumAmountAllowedException();
        }
        recordOperation(amount);
    }

    @Override
    public void withdraw(Amount amount) throws MinimumAmountAllowedException, InsufficientFundsForWithdrawalException {
        Amount negativeAmount = negateAmount(amount);
        if(isNotValidMinimumAmount(negativeAmount)){
            throw new MinimumAmountAllowedException();
        }
        if(isNotValidMaximumWithdrawalAmount(negativeAmount)){
            throw new InsufficientFundsForWithdrawalException();
        }
        recordOperation(negativeAmount);
    }

    @Override
    public void printStatement() {
        statementPrinting.printStatement(operationsRecord.retrieveAllOperations());
    }

    private boolean isNotValidMinimumAmount(Amount amount){
        return amount.getAmountValue().abs().compareTo(BigDecimal.ZERO) == 0;
    }

    private boolean isNotValidMaximumWithdrawalAmount(Amount amount){
        BigDecimal balanceValue = retrieveBalanceFromAccountStatements(operationsRecord.retrieveAllOperations()).getAmountValue();
        return amount.getAmountValue().abs().compareTo(balanceValue) > 0;
    }

    private Amount retrieveBalanceFromAccountStatements(List<Operation> Operations){
        AtomicReference<BigDecimal> balance = new AtomicReference<>(BigDecimal.ZERO);
        Stream<AccountStatement> accountStatementStream = Operations.stream().
                map(Operation -> new AccountStatement(Operation,
                        Amount.toAmount(balance.accumulateAndGet(Operation.amount().getAmountValue(), BigDecimal::add))));
        return accountStatementStream.
                map(AccountStatement::balance).reduce((first, second) -> second)
                .orElse(Amount.toAmount(balance.get()));
    }

    private Amount negateAmount(Amount amount){
        BigDecimal amountValue = amount.getAmountValue();
        if(amountValue.signum() < 0){
            return amount;
        }
        if(amountValue.signum() > 0){
            return new Amount(amountValue.negate());
        }
        return amount;
    }

    private void recordOperation(Amount amount) {
        var operation = new Operation(LocalDateTime.now(), amount);
        operationsRecord.recordOperation(operation);
    }

}
