package accounts;

import utils.LongId;

public class UserAccount {
    private final LongId<UserAccount> longId;
    private String login;
    private String password; //todo: убрать пасс из базы и перейти на хэш
    private Role role;

    public UserAccount(LongId<UserAccount> longId, String login, String password) {
        this.longId = longId;
        this.login = login;
        this.password = password;
        this.role = Role.USER;
    }

    public UserAccount(LongId<UserAccount> longId, String login, String password, Role role) {
        this.longId = longId;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public LongId<UserAccount> getLongId() {
        return longId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public enum Role {
        USER, ADMIN
    }
}
