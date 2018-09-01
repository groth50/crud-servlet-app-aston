package servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "GetIndex")
public class GetIndexPageServlet extends HttpServlet {
    public static final String PATH = "./index.jsp";
    static final Logger LOGGER = LogManager.getLogger(GetIndexPageServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.debug("doGet from " + this.getClass().getSimpleName());
        req.getRequestDispatcher(PATH).forward(req, resp);
    }
}
