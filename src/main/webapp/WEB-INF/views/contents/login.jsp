<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:import url="${path}/WEB-INF/views/include/header.jsp"/>

<form class="form-horizontal" method="POST" action="${path}/login">
    <div class="form-group">
        <label for="login-email" class="col-sm-2 control-label">Email</label>
        <div class="col-sm-10">
            <input type="email" name="email" class="form-control" id="login-email" placeholder="Email">
        </div>
    </div>
    <div class="form-group">
        <label for="login-pwd" class="col-sm-2 control-label">Password</label>
        <div class="col-sm-10">
            <input type="password" name="pwd" class="form-control" id="login-pwd" placeholder="Password">
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <div class="checkbox">
                <label>
                    <input type="checkbox"> Remember me
                </label>
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-default">Sign in</button>
        </div>
    </div>
</form>

<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>