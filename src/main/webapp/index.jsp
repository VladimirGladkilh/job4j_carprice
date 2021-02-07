<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<script>
    $(document).ready(function () {
        $("#myInput").on("keyup", function () {
            var value = $(this).val().toLowerCase();
            $("#myTable tr").filter(function () {
                $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
            });
        });
    });
    function openCard(id) {
        open("<%=request.getContextPath()%>/car.do?carId=" + id, this);
    }
</script>
<div class="container">
    <div class="panel-heading text-right">
        <h5>
            <% if (request.getAttribute("user") != null) {%>
            <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp">Привет <c:out value="${user.name}"/>
                |
                Выход</a>
            <%} else {%>
            <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp">Войти</a>
            <%}%>
        </h5>
    </div>
</div>
<div class="panel-body">
    <table class="table">
        <tr>
            <th>
                <label>Объявления о продаже автомобилей</label>
            </th>
            <th>
                <label for="myInput"></label><input class="form-control" id="myInput" type="text" placeholder="Найти..">
            </th>
        </tr>
    </table>

    <div class="container">
        <div class="panel-heading text-right">
            <h5>
                <a class="nav-link" href='<c:url value="/car.do?carId=0"/>'>Добавить</a>
            </h5>
        </div>
    </div>
    <table id="myTable" class="table table-bordered">
        <thead>
        <tr>
            <th scope="col"></th>
            <th scope="col">Марка</th>
            <th scope="col">Модель</th>
            <th scope="col">Год выпуска</th>
            <th scope="col">Кузов</th>
            <th scope="col">Коробка</th>
            <th scope="col">Привод</th>
            <th scope="col">Тип двигателя</th>
            <th scope="col">Пробег</th>
            <th scope="col">Цена</th>
            <th scope="col">Продано</th>
        </tr>
        </thead>
        <tbody>

        <c:forEach items="${requestScope.cars}" var="car">
            <tr onclick='openCard("${car.id}")'>
                <td>
                    <c:if test="${car.mainPhoto != null}">
                    <img src='<c:url value="/download?path=${car.mainPhoto.path}"/>'
                         class="img-responsive" width="100px" height="100px">
                    </c:if>
                    <c:if test="${car.mainPhoto == null}">
                        <img src='<c:url value="/download?path=imgDefault.png"/>'
                             class="img-responsive" width="100px" height="100px">
                    </c:if>
                </td>
                <td>
                    <c:out value="${car.marka.name}"/>
                </td>
                <td>
                    <c:out value="${car.model.name}"/>
                </td>
                <td>
                    <c:out value="${car.year}"/>
                </td>
                <td>
                    <c:out value="${car.body.name()}"/>
                </td>
                <td>
                    <c:out value="${car.gear.name()}"/>
                </td>
                <td>
                    <c:out value="${car.privod.name()}"/>
                </td>
                <td>
                    <c:out value="${car.engineType.name()}"/>
                </td>
                <td>
                    <c:out value="${car.probeg}"/>
                </td>
                <td>
                    <c:out value="${car.price}"/>
                </td>
                <td>
                <input type="checkbox" id="${car.id}" ${car.saled ? "checked" : ""} disabled/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>
