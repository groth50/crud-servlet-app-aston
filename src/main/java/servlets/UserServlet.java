package servlets;

import database.DBUtil;
import entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.RoleService;
import services.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "User", urlPatterns = "/user")
public class UserServlet extends HttpServlet {
    public static final String URL = "/user";
    static final Logger LOGGER = LogManager.getLogger(UserServlet.class.getName());
    private UserService userService;

    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        super.init();
        this.mapper = new ObjectMapper();
    }

    @Override
    public void destroy() {
        super.destroy();
        this.mapper = null;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("doGet from " + this.getClass().getSimpleName());

        try (Connection connection = DBUtil.getMysqlConnection()) {
            UserService userService = new UserService(connection);

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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("doPost from " + this.getClass().getSimpleName());

        try (Connection connection = DBUtil.getMysqlConnection()) {
            UserService userService = new UserService(connection);
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("doPut from " + this.getClass().getSimpleName());

        try (Connection connection = DBUtil.getMysqlConnection()) {
            UserService userService = new UserService(connection);
            String body = getBody(request);

            User user = mapper.readValue(body, User.class);

            if (user.getUserName() == null || user.getUserName().isEmpty()) {
                LOGGER.debug("null userName");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            try {
                boolean isUpdated = userService.update(user);
                if (isUpdated) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("doDelete from " + this.getClass().getSimpleName());
        try (Connection connection = DBUtil.getMysqlConnection()) {
            UserService userService = new UserService(connection);
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getBody(HttpServletRequest request) throws IOException {
        return request.getReader().lines()
                .reduce("", (accumulator, actual) -> accumulator + actual);
    }
}
