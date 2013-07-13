package name.abuchen.portfolio;

import java.util.UUID;

import name.abuchen.portfolio.model.Account;
import name.abuchen.portfolio.model.AccountTransaction;
import name.abuchen.portfolio.model.AccountTransaction.Type;
import name.abuchen.portfolio.model.Client;

import org.joda.time.DateMidnight;

public class AccountBuilder
{
    private Account account;

    public AccountBuilder()
    {
        this.account = new Account();
        this.account.setName(UUID.randomUUID().toString());
    }

    public Account addTo(Client client)
    {
        client.addAccount(account);
        return account;
    }

    public AccountBuilder deposit_(String date, long amount)
    {
        return transaction(Type.DEPOSIT, date, amount);
    }

    public AccountBuilder deposit_(DateMidnight date, long amount)
    {
        return transaction(Type.DEPOSIT, date, amount);
    }

    public AccountBuilder interest(String date, long amount)
    {
        return transaction(Type.INTEREST, date, amount);
    }

    public AccountBuilder interest(DateMidnight date, long amount)
    {
        return transaction(Type.INTEREST, date, amount);
    }

    public AccountBuilder fees____(String date, long amount)
    {
        return transaction(Type.FEES, date, amount);
    }

    public AccountBuilder fees____(DateMidnight date, long amount)
    {
        return transaction(Type.FEES, date, amount);
    }

    public AccountBuilder withdraw(String date, long amount)
    {
        return transaction(Type.REMOVAL, date, amount);
    }

    public AccountBuilder withdraw(DateMidnight date, long amount)
    {
        return transaction(Type.REMOVAL, date, amount);
    }

    private AccountBuilder transaction(Type type, String date, long amount)
    {
        return transaction(type, new DateMidnight(date), amount);
    }

    private AccountBuilder transaction(Type type, DateMidnight date, long amount)
    {
        AccountTransaction t = new AccountTransaction(date.toDate(), null, type, amount);
        account.addTransaction(t);
        return this;
    }

}
