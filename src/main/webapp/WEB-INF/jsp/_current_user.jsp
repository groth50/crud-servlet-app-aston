<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div>
    Your login <c:out value="${currentUser.login}" />
    <form action="${pageContext.request.contextPath}/logout" method="POST" class="form">
    <fieldset>
        <div class="form-group">
            <input type="submit" value="Log out">
        </div>
    </fieldset>
    </form>
</div>
