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
    <link rel="icon" type="image/png" href="/carprice/favicon.ico"/>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
<body onload="loadData()">
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
    function loadData() {
        getMarkaData();
        getModelList();
        getEnumList("body");
        getEnumList("gear");
        getEnumList("engineType");
        getEnumList("privod")
    }

    function getMarkaData() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/carprice/model.do?action=marka",
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

    function getModelList() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/carprice/model.do?action=model",
            data: "markaId=" + $('#marka').val(),
            dataType: 'json',
            origin: "http://localhost:8081"
        })
            .done(function (data) {
                var modelId = $('#model').val();
                let model = "<option value=\"\"></option>";
                for (let i = 0; i < data.length; i++) {
                    if (modelId === data[i]['id']) {
                        model += "<option value=" + data[i]['id'] + " selected>" + data[i]['name'] + "</option>";
                    } else {
                        model += "<option value=" + data[i]['id'] + ">" + data[i]['name'] + "</option>";
                    }
                }
                $('#model').html(model);
            })
            .fail(function (err) {
                alert("err " + err.message);
            })
    }

    function getEnumList(enumType) {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/carprice/model.do?",
            data: "action=" + enumType,
            dataType: 'json',
            origin: "http://localhost:8081"
        })
            .done(function (data) {
                let model = "<option value=\"\"></option>";
                for (let i = 0; i < data.length; i++) {
                    if ((enumType === "body" && "<%=car.getBody().name() %>" === data[i]['id'])
                        || (enumType === "gear" && "<%= car.getGear().name() %>" === data[i]['id'])
                        || (enumType === "engineType" && "<%= car.getEngineType().name() %>" === data[i]['id'])
                        || (enumType === "privod" && "<%= car.getPrivod().name() %>" === data[i]['id'])
                    ) {
                        model += "<option value=" + data[i]['id'] + " selected>" + data[i]['name'] + "</option>";
                    } else {
                        model += "<option value=" + data[i]['id'] + ">" + data[i]['name'] + "</option>";
                    }
                }

                $('#'+enumType).html(model);
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
            Добавляем объявление
            <% } else { %>
            Редактируем объявление
            <% } %>
        </div>
        <div class="card-body">
            <!--% if (request.getAttribute("user") != null) {%-->
            <form action="<%=request.getContextPath()%>/car.do?id=<%=car.getId()%>" method="post"
                  enctype="multipart/form-data">
                <div class="form-group">
                    <label for="marka">Марка</label>
                    <select class="form-control" id="marka" name="marka" onchange="getModelList()"></select>
                    <label for="model">Модель</label>
                    <select class="form-control" id="model" name="model"></select>
                    <label for="year">Год</label>
                    <input type="text" name="year" id="year" value="<%= car.getYear() %>"/>
                    <label for="body">Кузов</label>
                    <select class="form-control" name="body" id="body"><%= car.getBody().name() %></select>
                    <label for="gear">Коробка</label>
                    <select class="form-control" name="gear" id="gear"><%= car.getGear().name() %></select>
                    <label for="engineType">Двигатель</label>
                    <select class="form-control" name="engineType" id="engineType"><%= car.getEngineType().name() %></select>
                    <label for="privod">Привод</label>
                    <select class="form-control" name="privod" id="privod"><%= car.getPrivod().name() %></select>
                    <label for="description">Описание</label>
                    <input type="text" size="3" class="form-control" name="description"
                           id="description" value="<%= car.getDescription() %>"/>
                    <div>
                        <label for="image">Фото</label>
                        <% if (car.getMainPhoto() == null) {%>
                        <img src='/carprice/download?path=imgDefault.png'
                             class="img-thumbnail" width="100px" id="image" name="image" height="100px">

                        <%} else {%>
                        <img src="<%=car.getMainPhoto().getPath()%>" width="100px" height="100px" class="img-thumbnail" alt="Image not found">
                        <%}%>
                        <input type="file" class="form-control" name="image" id="image">
                        <a href="#">+</a>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary">Сохранить</button>
                <button type="button" class="btn btn-primary" name="back" onclick="history.back()">back</button>
            </form>
        </div>
    </div>
</div>


</body>
</html>
