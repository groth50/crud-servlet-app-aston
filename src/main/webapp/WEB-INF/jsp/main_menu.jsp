<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 01.08.2018
  Time: 18:32
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
    <title>Main menu</title>
</head>
<body>
<div>
    Main menu
</div>

<div>
    <a href="${pageContext.request.contextPath}/adminmenu">Admin menu</a>
</div>

<jsp:include page="/WEB-INF/jsp/_current_user.jsp"></jsp:include>

<div>
    All user profile
</div>
<table border=1>
    <thead>
    <tr>
        <th>User Id</th>
        <th>Login</th>
        <th>Role</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${users}" var="user">
        <tr>
            <td><c:out value="${user.longId.id}" /></td>
            <td><c:out value="${user.login}" /></td>
            <td><c:out value="${user.role}" /></td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<form action="${pageContext.request.contextPath}/logout" method="POST" class="form">
    <fieldset>
        <div class="form-group">
            <input type="submit" value="Log out">
        </div>
    </fieldset>
</form>

<jsp:include page="/WEB-INF/jsp/_message_box.jsp"></jsp:include>

</body>
</html>
