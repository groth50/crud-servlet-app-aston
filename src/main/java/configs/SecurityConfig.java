package configs;

import accounts.UserAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SecurityConfig {


    // String: Role
    // List<String>: urlPatterns.
    private static final Map<UserAccount.Role, List<String>> mapConfig = new HashMap<>();

    static {
        init();
    }

    private static void init() {

        // Конфигурация для роли "USER".
        List<String> urlPatternsUser = new ArrayList<String>();

        urlPatternsUser.add("/mainmenu");

        mapConfig.put(UserAccount.Role.USER, urlPatternsUser);

        // Конфигурация для роли "ADMIN".
        List<String> urlPatternsAdmin = new ArrayList<String>();

        urlPatternsAdmin.add("/adminmenu");
        urlPatternsAdmin.add("/mainmenu");
        urlPatternsAdmin.add("/adduser");
        urlPatternsAdmin.add("/deleteuser");
        urlPatternsAdmin.add("/updateuser");

        mapConfig.put(UserAccount.Role.ADMIN, urlPatternsAdmin);
    }

    public static Set<UserAccount.Role> getAllAppRoles() {
        return mapConfig.keySet();
    }

    public static List<String> getServletPathForRole(UserAccount.Role role) {
        return mapConfig.get(role);
    }

}
