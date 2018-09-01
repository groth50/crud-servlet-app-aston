package utils;
import accounts.UserAccount;
import configs.SecurityConfig;

import java.util.List;

public class SecurityUtil {

    //todo: только нафига? у меня маппинг в фильтре этим занимается
    // Проверить требует ли данный 'request' входа в систему или нет.
//    public static boolean isSecurityPage(HttpServletRequest request) {
//        String urlPattern = UrlPatternUtil.getUrlPattern(request);
//
//        Set<UserAccount.Role> roles = SecurityConfig.getAllAppRoles();
//
//        for (UserAccount.Role role : roles) {
//            List<String> urlPatterns = SecurityConfig.getServletPathForRole(role);
//            if (urlPatterns != null && urlPatterns.contains(urlPattern)) {
//                return true;
//            }
//        }
//        return false;
//    }

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
