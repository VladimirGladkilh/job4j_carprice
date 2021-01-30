<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
    <title>-=CarPrice=- Сдай свое ведро в утиль и купи другое ведро</title>
    <link rel="icon" type="image/png" href="favicon.ico"/>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
<body>
<div class="row">

    <% if (request.getAttribute("user") != null) {%>
    <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp">Привет <c:out value="${user.name}"/> |
        Выход</a>
    <%} else {%>
    <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp">Войти</a>
    <%}%>

</div>
<div class="container">
    <h2>Объявления о продаже автомобилей</h2>
    <table class="table table-bordered">
        <thead>
        <tr>
            <th scope="col">РќР°Р·РІР°РЅРёСЏ</th>
        </tr>
        </thead>
        <tbody>

        <c:forEach items="${cars}" var="car">
            <tr>
                <td>
                    <c:out value="${car.marka.name}"/>
                </td>
                <td>
                    <c:out value="${car.model.name}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>
