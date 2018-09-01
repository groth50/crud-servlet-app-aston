package servlet;

import accounts.AccountService;
import accounts.FactoryAccountService;
import accounts.UserAccount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.PageMessageUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UpdateUser", urlPatterns = "/updateuser")
public class UpdateUserServlet extends HttpServlet {
    static final Logger LOGGER = LogManager.getLogger(UpdateUserServlet.class.getName());
    public static final String PATH = "./WEB-INF/jsp/update_user.jsp";
    public static final String URL = "/updateuser";

    private AccountService accountService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.accountService = FactoryAccountService.getAccountService();
    }

    @Override
    public void destroy() {
        super.destroy();
        this.accountService = null;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("doGet from " + this.getClass().getSimpleName());

        PageMessageUtil.clearPageMessageForDoGet(request);
        //check param from page
        String login = request.getParameter("login");
        if (login == null) {
            request.setAttribute("errorMessage", "Incorrect login.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher(GetAdminMenuServlet.PATH).forward(request, response);
            return;
        }
        //check login in base
        //todo: убрать пароль из полей
        UserAccount user = accountService.getUserByLogin(login);
        if (user == null) {
            request.setAttribute("errorMessage", "User not found.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher(GetAdminMenuServlet.PATH).forward(request, response);
            return;
        }

        request.setAttribute("user", user);
        request.setAttribute("adminRole", UserAccount.Role.ADMIN);
        request.setAttribute("userRole", UserAccount.Role.USER);
        response.setStatus(HttpServletResponse.SC_OK);
        request.getRequestDispatcher(PATH).forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("doPost from " + this.getClass().getSimpleName());
        PageMessageUtil.clearPageMessageForDoPost(request);
        String newLogin = request.getParameter("login");
        String password = request.getParameter("password");
        String roleString = request.getParameter("role");

        if (newLogin == null || password == null || roleString == null || newLogin.isEmpty() || password.isEmpty() || roleString.isEmpty()) {
            response.setContentType("text/html;charset=utf-8");
            request.setAttribute("errorMessage", "Please, insert correct login, role and password");
            UserAccount user = accountService.getUserByLogin(request.getParameter("oldLogin")); //при вводе неправильных данных заполняем поля как было
            request.setAttribute("user", user);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher(PATH).forward(request, response);
            return;
        }

        String oldLogin = request.getParameter("oldLogin");

        UserAccount user = accountService.getUserByLogin(oldLogin);
        user.setLogin(newLogin);
        user.setPassword(password);
        if (roleString != null) {
            UserAccount.Role role = UserAccount.Role.valueOf(roleString.toUpperCase());
            user.setRole(role);
        }

        response.setContentType("text/html;charset=utf-8");
        request.setAttribute("successMessage", "Update successful");
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect(request.getContextPath() + GetAdminMenuServlet.URL);
    }
}
