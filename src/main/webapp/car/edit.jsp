<%@ page import="model.Car" %>
<%@ page import="model.Photo" %>
<%@ page import="store.HbmStore" %>
<%@ page import="model.Marka" %>
<%@ page import="model.Model" %><%--
  Created by IntelliJ IDEA.
  User: Gladkih
  Date: 30.01.2021
  Time: 22:59
  To change this template use File | Settings | File Templates.
  Страница для просмотра/редактирования объявления
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
<body onload="getMetaData()">
<%
    String id = request.getParameter("id");
    Car car = new Car();
    Photo photo = new Photo(0, "");
    Marka marka = new Marka();
    Model model = new Model();
    if (id != null) {
        car = HbmStore.instOf().findById(Car.class, Integer.parseInt(id));
        if (car != null) {
            if (car.getMainPhoto() != null) {
                photo = car.getMainPhoto();
            }
            marka = car.getMarka();
            model = car.getModel();
        }
    }
%>
<script>
    function getMetaData() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/carprice/marka.do?list=true",
            dataType: 'json',
            origin: "http://localhost:8081"
        })
            .done(function (data) {
                var markaId = <%=marka.getId()%>;
                let markas = "<option value=\"\"></option>";
                for (let i = 0; i < data.length; i++) {
                    if (markaId === data[i]['id']) {
                        markas += "<option value=" + data[i]['id'] + " selected>" + data[i]['name'] + "</option>";
                    } else {
                        markas += "<option value=" + data[i]['id'] + ">" + data[i]['name'] + "</option>";
                    }
                }
                $('#marka').html(markas);
            })
            .fail(function (err) {
                alert("err " + err.message);
            })
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
<div class="container">
    <div class="card" style="width: 100%">
        <div class="card-header">
            <% if (id == null) { %>
            Добавляем объявление.
            <% } else { %>
            Редактируем объявление.
            <% } %>
        </div>
        <div class="card-body">
            <% if (request.getAttribute("user") != null) {%>
            <form action="<%=request.getContextPath()%>/car.do?id=<%=car.getId()%>" method="post"
                  enctype="multipart/form-data">
                <div class="form-group">
                    <label>РРјСЏ</label>
                    <input type="text" class="form-control" name="name" value="<%=candidate.getName()%>">
                    <a href="<%=request.getContextPath()%>/download?path=<%=photo.getPath()%>">Download</a>
                    <img src="<%=request.getContextPath()%>/download?path=<%=photo.getPath()%>" width="100px"
                         height="100px"/>
                </div>
                <div class="checkbox">
                    <input type="file" class="form-control" name="image">
                </div>
                <div class="form-group">
                    <label for="city">Р“РѕСЂРѕРґ:</label>
                    <select class="form-control" id="city" name="cityId">
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">РЎРѕС…СЂР°РЅРёС‚СЊ</button>
                <button type="button" class="btn btn-primary" name="back" onclick="history.back()">back</button>
            </form>
            <%} else {%>

            <%}%>
        </div>
    </div>
</div>


</body>
</html>
