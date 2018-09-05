<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 05.09.2018
  Time: 19:38
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
    <title>
        User info
    </title>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/jsp/_menu.jsp"></jsp:include>
<div>
    Your profile
</div>
<div>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>User Id</th>
            <th>Login</th>
            <th>Role</th>
        </tr>
        </thead>
        <tr class="info">
            <td><c:out value="${currentUser.longId.id}" /></td>
            <td><c:out value="${currentUser.login}" /></td>
            <td><c:out value="${currentUser.role}" /></td>
            <td>
                <form action="${pageContext.request.contextPath}/logout" method="POST" class="form">
                    <fieldset>
                        <div class="form-group">
                            <input type="submit" value="Log out">
                        </div>
                    </fieldset>
                </form>
            </td>
        </tr>
    </table>

</div>
</body>
</html>
