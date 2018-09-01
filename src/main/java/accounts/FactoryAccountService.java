package accounts;

public class FactoryAccountService {
    public static final AccountServiceMock accountService = new AccountServiceMock();
    public static AccountService getAccountService() {
        return accountService;
    }
}
