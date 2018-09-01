package accounts;

import utils.LongId;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

//todo: завязать через DAO
public class AccountServiceMock implements AccountService {
    private final Map<String, UserAccount> loginToProfile;
    private final Map<String, UserAccount> sessionIdToProfile;
    private AtomicLong id = new AtomicLong(0);

    public AccountServiceMock() {
        loginToProfile = new HashMap<>();
        sessionIdToProfile = new HashMap<>();
        loginToProfile.put("test", new UserAccount(new LongId<UserAccount>(-2), "test", "123", UserAccount.Role.USER));
        loginToProfile.put("admin", new UserAccount(new LongId<UserAccount>(-1), "admin", "admin", UserAccount.Role.ADMIN));
    }

    @Override
    public void addNewUser(String login, String password) {
        loginToProfile.put(login, new UserAccount(new LongId<UserAccount>(id.incrementAndGet()), login, password));
    }

    @Override
    public void deleteUser(String login) {
        loginToProfile.remove(login);
    }


    @Override
    public UserAccount getUserByLogin(String login) {
        return loginToProfile.get(login);
    }

    //todo: реализовать
    public UserAccount getUserById(String id) { return null; }

    @Override
    public UserAccount getUserBySessionId(String sessionId) {
        return sessionIdToProfile.get(sessionId);
    }

    @Override
    public void addSession(String sessionId, UserAccount userAccount) {
        sessionIdToProfile.put(sessionId, userAccount);
    }

    @Override
    public void deleteSession(String sessionId) {
        sessionIdToProfile.remove(sessionId);
    }

    @Override
    public Collection<UserAccount> getAllUsers() {
        return loginToProfile.values();
    }
}
