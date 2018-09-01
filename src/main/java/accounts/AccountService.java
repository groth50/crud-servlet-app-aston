package accounts;

import java.util.Collection;
import java.util.Set;

public interface AccountService {

    void addNewUser(String login, String password);

    void deleteUser(String login);

    UserAccount getUserByLogin(String login);

    UserAccount getUserBySessionId(String sessionId);

    void addSession(String sessionId, UserAccount userAccount);

    void deleteSession(String sessionId);

    Collection<UserAccount> getAllUsers();
}
