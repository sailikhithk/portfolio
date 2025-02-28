package name.abuchen.portfolio.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import name.abuchen.portfolio.money.Money;
import name.abuchen.portfolio.money.Values;

public class AccountTransaction extends Transaction
{
    public enum Type
    {
        DEPOSIT(false), REMOVAL(true), //
        INTEREST(false), INTEREST_CHARGE(true), //
        DIVIDENDS(false), //
        FEES(true), FEES_REFUND(false), //
        TAXES(true), TAX_REFUND(false), //
        BUY(true), SELL(false), //
        TRANSFER_IN(false), TRANSFER_OUT(true);

        private static final ResourceBundle RESOURCES = ResourceBundle.getBundle("name.abuchen.portfolio.model.labels"); //$NON-NLS-1$

        private final boolean isDebit;

        private Type(boolean isDebit)
        {
            this.isDebit = isDebit;
        }

        public boolean isDebit()
        {
            return isDebit;
        }

        public boolean isCredit()
        {
            return !isDebit;
        }

        @Override
        public String toString()
        {
            return RESOURCES.getString("account." + name()); //$NON-NLS-1$
        }
    }

    private Type type;

    public AccountTransaction()
    {
        // needed for xstream de-serialization
    }

    /* protobuf only */ AccountTransaction(String uuid)
    {
        super(uuid);
    }

    public AccountTransaction(LocalDateTime date, String currencyCode, long amount, Security security, Type type)
    {
        super(date, currencyCode, amount, security, 0, null);
        this.type = type;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
        setUpdatedAt(Instant.now());
    }

    /**
     * Returns the gross value, i.e. the value including taxes. See
     * {@link #getGrossValue()}.
     */
    public long getGrossValueAmount()
    {
        long taxAndFees = getUnitSum(Unit.Type.FEE, Unit.Type.TAX).getAmount();
        return getAmount() + (type.isCredit() ? taxAndFees : -taxAndFees);
    }

    /**
     * Returns the gross value, i.e. the value before taxes are applied. At the
     * moment, only dividend transactions are supported.
     */
    public Money getGrossValue()
    {
        return Money.of(getCurrencyCode(), getGrossValueAmount());
    }

    @Override
    public String toString()
    {
        return String.format("%s %-17s %s %9s %s %s", //$NON-NLS-1$
                        Values.Date.format(getDateTime().toLocalDate()), //
                        type.name(), //
                        getCurrencyCode(), //
                        Values.Amount.format(getAmount()), //
                        getSecurity() != null ? getSecurity().getName() : "<no Security>", //$NON-NLS-1$
                        getCrossEntry() != null && getCrossEntry().getCrossOwner(this) != null
                                        ? getCrossEntry().getCrossOwner(this).toString()
                                        : "<no XEntry>" //$NON-NLS-1$
        );
    }
}
