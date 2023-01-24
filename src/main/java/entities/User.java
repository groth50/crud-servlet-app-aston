package entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties (ignoreUnknown = true)
public class User {
    private long userId;
    private String userName;
    private long roleId;

    public User() {
    }

    public User(long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.roleId = Role.DEFAULT_ROLE;
    }

    public User(String userName) {
        this.userName = userName;
        this.roleId = Role.DEFAULT_ROLE;
    }

    public User( String userName, long roleId) {
        this.userName = userName;
        this.roleId = roleId;
    }

    public User(long userId, String userName, long roleId) {
        this.userId = userId;
        this.userName = userName;
        this.roleId = roleId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getRoleId() {
        return roleId;
    }
    public void setRoleId(long roleId) {this.roleId = roleId;}

}
