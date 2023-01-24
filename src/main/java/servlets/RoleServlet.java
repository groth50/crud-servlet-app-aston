package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import database.DBUtil;
import entities.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.RoleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "Role", urlPatterns = "/role")
public class RoleServlet extends HttpServlet {
    public static final String URL = "/role";
    static final Logger LOGGER = LogManager.getLogger(RoleServlet.class.getName());

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
            RoleService roleService = new RoleService(connection);
            String roleId = request.getParameter("id");
            String result;
            if (roleId == null) {
                result = mapper.writeValueAsString(roleService.getAll());
            } else {
                Role role = roleService.get(Integer.parseInt(roleId));
                result = mapper.writeValueAsString(role);
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
            RoleService roleService = new RoleService(connection);
            String body = getBody(request);

            Role role = mapper.readValue(body, Role.class);

            if (role.getRoleName() == null || role.getRoleName().isEmpty()) {
                LOGGER.debug("null role name");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            try {
                long roleId = roleService.create(role);
                String result = mapper.writeValueAsString(role);

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
            RoleService roleService = new RoleService(connection);
            String body = getBody(request);

            Role role = mapper.readValue(body, Role.class);

            if (role.getRoleName() == null || role.getRoleName().isEmpty()) {
                LOGGER.debug("null role name");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            try {
                boolean isUpdated = roleService.update(role);
                if (isUpdated) {
                    String result = mapper.writeValueAsString(role);

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
            RoleService roleService = new RoleService(connection);
            String body = getBody(request);


            Role role = mapper.readValue(body, Role.class);

            if (role.getRoleId() < 1) {
                LOGGER.debug("haven't role id");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            try {
                boolean isDeleted = roleService.delete(role.getRoleId());
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
