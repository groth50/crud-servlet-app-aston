package servlets;

import database.DBService;
import entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "User", urlPatterns = "/user")
public class UserServlet extends HttpServlet {
    public static final String URL = "/user";
    static final Logger LOGGER = LogManager.getLogger(UserServlet.class.getName());
    private UserService userService;

    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserService(DBService.getMysqlConnection());
        this.mapper = new ObjectMapper();
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public void destroy() {
        super.destroy();
        this.userService = null;
        this.mapper = null;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("doGet from " + this.getClass().getSimpleName());

        String userId = request.getParameter("id");
        String result;
        if (userId == null) {
            result = mapper.writeValueAsString(userService.getAll());
        } else {
            User user = userService.get(Integer.parseInt(userId));
            result = mapper.writeValueAsString(user);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(result);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("doPost from " + this.getClass().getSimpleName());
        String body = getBody(request);

        User user = mapper.readValue(body, User.class);

        if (user.getUserName() == null || user.getUserName().isEmpty()) {
            LOGGER.debug("null userName");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            long userId = userService.create(user);
            String result = mapper.writeValueAsString(user);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(result);
        } catch (RuntimeException e) {
            LOGGER.error(e.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("doPut from " + this.getClass().getSimpleName());
        String body = getBody(request);

        User user = mapper.readValue(body, User.class);

        if (user.getUserName() == null || user.getUserName().isEmpty()) {
            LOGGER.debug("null userName");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            boolean isCreated = userService.update(user);
            if (isCreated) {
                String result = mapper.writeValueAsString(user);

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write(result);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (RuntimeException e) {
            LOGGER.error(e.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("doDelete from " + this.getClass().getSimpleName());
        String body = getBody(request);


        User user = mapper.readValue(body, User.class);

        if (user.getUserId() < 1) {
            LOGGER.debug("haven't userId");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            boolean isDeleted = userService.delete(user.getUserId());
            if (isDeleted) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (RuntimeException e) {
            LOGGER.error(e.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private String getBody(HttpServletRequest request) throws IOException {
        return request.getReader().lines()
                .reduce("", (accumulator, actual) -> accumulator + actual);
    }
}
