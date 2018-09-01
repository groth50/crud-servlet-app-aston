<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div>
    Your profile
</div>
<table border=0>
    <thead>
    <tr>
        <th>User Id</th>
        <th>Login</th>
        <th>Role</th>
    </tr>
    </thead>
    <tbody>
        <tr>
            <td><c:out value="${currentUser.longId.id}" /></td>
            <td><c:out value="${currentUser.login}" /></td>
            <td><c:out value="${currentUser.role}" /></td>
        </tr>
    </tbody>
</table>
