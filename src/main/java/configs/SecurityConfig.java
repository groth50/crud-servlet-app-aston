package configs;

import accounts.UserAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Класс SecurityConfig помогает конфигурировать роли и их доступ к функциям приложения (к страницам).
 */
public class SecurityConfig {
    /**
     * UserAccount.Role: Role
     * List<String>: urlPatterns
     */
    private static final Map<UserAccount.Role, List<String>> mapConfig = new HashMap<>();

    static {
        init();
    }

    /**
     * Формирует для каждой роли список доступных страниц
     */
    private static void init() {
        // Конфигурация для роли "USER".
        List<String> urlPatternsUser = new ArrayList<String>();

        urlPatternsUser.add("/mainmenu");
        urlPatternsUser.add("/userinfo");

        mapConfig.put(UserAccount.Role.USER, urlPatternsUser);

        // Конфигурация для роли "ADMIN".
        List<String> urlPatternsAdmin = new ArrayList<String>();

        urlPatternsAdmin.add("/adminmenu");
        urlPatternsAdmin.add("/mainmenu");
        urlPatternsAdmin.add("/adduser");
        urlPatternsAdmin.add("/deleteuser");
        urlPatternsAdmin.add("/updateuser");
        urlPatternsAdmin.add("/userinfo");

        mapConfig.put(UserAccount.Role.ADMIN, urlPatternsAdmin);
    }

    /**
     * Возвращает множество всех ролей в приложении
     */
    public static Set<UserAccount.Role> getAllAppRoles() {
        return mapConfig.keySet();
    }

    /**
     * Возвращает список доступных страниц для переданной роли
     */
    public static List<String> getServletPathForRole(UserAccount.Role role) {
        return mapConfig.get(role);
    }

}
