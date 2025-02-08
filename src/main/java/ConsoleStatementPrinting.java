import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class ConsoleStatementPrinting implements StatementPrinting {
    private static final String STATEMENT_TITLE = "   Date    || Amount || Balance\n";

    @Override
    public void printStatement(List<Operation> operations) {
        StringBuilder statementPrinting = new StringBuilder();
        statementPrinting.append(STATEMENT_TITLE);
        buildAccountStatements(operations).
                sorted(Comparator.comparing(AccountStatement::getOperationDate).reversed()).
                forEach(accountStatement -> {
                    statementPrinting.append(format(accountStatement));
                });
        System.out.print(statementPrinting);
    }

    private Stream<AccountStatement> buildAccountStatements(List<Operation> Operations){
        AtomicReference<BigDecimal> balance = new AtomicReference<>(BigDecimal.ZERO);
        return Operations.stream().
                map(Operation -> new AccountStatement(Operation,
                        Amount.toAmount(balance.accumulateAndGet(Operation.amount().getAmountValue(), BigDecimal::add))));
    }

    private String format(AccountStatement accountStatement) {
        DecimalFormatSymbols spaceGroupingSeparatorSymbol = new DecimalFormatSymbols();
        spaceGroupingSeparatorSymbol.setDecimalSeparator('.');
        spaceGroupingSeparatorSymbol.setGroupingSeparator(' ');
        DecimalFormat amountFormatter = new DecimalFormat();
        amountFormatter.setMinimumFractionDigits(2);
        amountFormatter.setMaximumFractionDigits(2);
        amountFormatter.setGroupingUsed(true);
        amountFormatter.setDecimalFormatSymbols(spaceGroupingSeparatorSymbol);

        return String.format("%s || %s || %s\n",
                accountStatement.getOperationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                amountFormatter.format(accountStatement.Operation().amount().getAmountValue()),
                amountFormatter.format(accountStatement.balance().getAmountValue()));
    }

}
