package utils;
import accounts.UserAccount;
import configs.SecurityConfig;

import java.util.List;

public class SecurityUtil {

    // Проверить имеет ли данный 'request' подходящую роль?
    public static boolean hasPermission(String servletPath, UserAccount.Role role) {

        List<String> servletPathForRole = SecurityConfig.getServletPathForRole(role);
        return servletPathForRole.contains(servletPath);
    }

    //проверка - это страница для входа или регистрации?
    public static boolean isLoginPage(String servletPath) {
        return (servletPath.equals("/signin") || servletPath.equals("/signup"));
    }


}
