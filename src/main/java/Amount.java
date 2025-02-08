import java.math.BigDecimal;

public final class Amount {

    private final BigDecimal amountValue;

    public Amount(BigDecimal amountValue) {
        this.amountValue = amountValue;
    }

    public Amount(int amountValue) {
        this.amountValue = BigDecimal.valueOf(amountValue);
    }

    public static Amount toAmount(BigDecimal amountValue) {
        return new Amount(amountValue);
    }

    public static Amount toAmount(int amountValue) {
        return new Amount(amountValue);
    }

    public BigDecimal getAmountValue() {
        return this.amountValue;
    }

}
