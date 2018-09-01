package servlet;

import accounts.AccountService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import accounts.FactoryAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet(name = "DeleteUser", urlPatterns = "/deleteuser")
public class DeleteUserServlet extends HttpServlet {
    private AccountService accountService;
    static final Logger LOGGER = LogManager.getLogger(DeleteUserServlet.class.getName());
    public static final String URL = "/deleteuser";

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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("doPost from " + this.getClass().getSimpleName());
        String login = request.getParameter("userLogin");
        String Id = request.getParameter("userId");

        accountService.deleteUser(login);
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect(GetAdminMenuServlet.URL);
    }
}
