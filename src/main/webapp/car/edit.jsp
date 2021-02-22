<%@ page import="store.HbmStore" %>
<%@ page import="model.*" %><%--
  Created by IntelliJ IDEA.
  User: Gladkih
  Date: 30.01.2021
  Time: 22:59
  To change this template use File | Settings | File Templates.
  Страница для просмотра/редактирования объявления
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    int id = Integer.parseInt(request.getParameter("carId"));
    Car car = new Car(0);
    Photo photo = new Photo(0, "");
    Marka marka = new Marka();
    Model model = new Model();
    if (id > 0) {
        car = HbmStore.instOf().findById(Car.class, id);
        if (car != null) {
            if (car.viewMainPhoto() != null) {
                photo = car.viewMainPhoto();
            }
            marka = car.getMarka();
            model = car.getModel();
        }
    }
%>
<script>
    function loadData() {
        getMarkaData();
        getModelList()
        getEnumList("body");
        getEnumList("gear");
        getEnumList("engineType");
        getEnumList("privod")
    }

    function getMarkaData() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/carprice/model.do?carId=<%= car.getId()%>&action=marka",
            dataType: 'json',
            origin: "http://localhost:8080"
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
            url: "http://localhost:8080/carprice/model.do?carId=<%= car.getId()%>&action=model",
            data: "markaId=" + $('#marka').val(),
            dataType: 'json',
            origin: "http://localhost:8080"
        })
            .done(function (data) {
                var modelId = <%=model.getId()%>;
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
            url: "http://localhost:8080/carprice/model.do?carId=<%= car.getId()%>&",
            data: "action=" + enumType,
            dataType: 'json',
            origin: "http://localhost:8080"
        })
            .done(function (data) {
                let model = "<option value=\"\"></option>";
                for (let i = 0; i < data.length; i++) {
                    if ("<%=car.getId()%>" !== "0") {
                        if ((enumType === "body" && "<%=car.getBody() %>" === data[i]['id'])
                            || (enumType === "gear" && "<%= car.getGear() %>" === data[i]['id'])
                            || (enumType === "engineType" && "<%= car.getEngineType() %>" === data[i]['id'])
                            || (enumType === "privod" && "<%= car.getPrivod() %>" === data[i]['id'])
                        ) {
                            model += "<option value=" + data[i]['id'] + " selected>" + data[i]['name'] + "</option>";
                        } else {
                            model += "<option value=" + data[i]['id'] + ">" + data[i]['name'] + "</option>";
                        }
                    } else {
                        model += "<option value=" + data[i]['id'] + ">" + data[i]['name'] + "</option>";
                    }
                }

                $('#' + enumType).html(model);
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
            <% if (id == 0) { %>
            Добавляем объявление
            <% } else { %>
            Редактируем объявление
            <% } %>
        </div>
        <div class="card-body">

            <form action="<%=request.getContextPath()%>/car.do?carId=<%=car.getId()%>" method="post"
                  enctype="multipart/form-data">
                <div class="form-group">
                    <label for="marka">Марка</label>
                    <select class="form-control" id="marka" name="marka" onchange="getModelList()"></select>
                    <label for="model">Модель</label>
                    <select class="form-control" id="model" name="model"></select>
                    <label for="year">Год</label>
                    <input type="text" name="year" id="year" value="<%= car.getYear() %>"/>
                    <label for="probeg">Пробег</label>
                    <input type="text" name="probeg" id="probeg" value="<%= car.getProbeg() %>"/>
                    <label for="price">Цена</label>
                    <input type="text" name="price" id="price" value="<%= car.getPrice() %>"/>
                    <label for="body">Кузов</label>
                    <select class="form-control" name="body" id="body"><%= car.getBody() %>
                    </select>
                    <label for="gear">Коробка</label>
                    <select class="form-control" name="gear" id="gear"><%= car.getGear() %>
                    </select>
                    <label for="engineType">Двигатель</label>
                    <select class="form-control" name="engineType" id="engineType"><%= car.getEngineType() %>
                    </select>
                    <label for="privod">Привод</label>
                    <select class="form-control" name="privod" id="privod"><%= car.getPrivod() %>
                    </select>
                    <label for="description">Описание</label>
                    <input type="text" size="3" class="form-control" name="description"
                           id="description" value="<%= car.getDescription() %>"/>
                    <div>
                        <label for="image">Фото</label> <input type="file" class="form-control" name="image" id="image">
                        <% if (car.viewMainPhoto() == null) {%>
                        <img src='/carprice/download?path=imgDefault.png'
                             class="img-thumbnail" width="100px" id="image" name="image" height="100px">

                        <%} else {%>

                        <img src="<%=request.getContextPath()%>/download?path=<%=car.viewMainPhoto().getPath()%>" width="100px" height="100px" class="img-thumbnail"
                             alt="Image not found">
                        <div class="row">
                        <% int i = 1;
                        for (Photo phot: car.getPhotos()
                                ) {  %>
                            <div class="column">
                                <img src="<%=request.getContextPath()%>/download?path=<%=phot.getPath()%>"
                                     onclick="openModal();currentSlide(<%= i++ %>)" width="100px" height="100px" class="img-thumbnail">
                            </div>
                        <% } %>

                        </div>
                        <%}%>
                    </div>

                </div>
                <% if (request.getAttribute("user") != null) {
                    if (id == 0 || request.getAttribute("user").equals(car.getUser())) {%>
                <button type="submit" class="btn btn-primary">Сохранить</button>
                <%}%>
                <% if (!car.isSaled()) {%>
                <button type="button" class="btn btn-primary"
                        formaction="<%=request.getContextPath()%>/car.do?saleId=<%=car.getId()%>">Продано
                </button>
                <%}%>
                <%}%>
                <button type="button" class="btn btn-primary" name="back" onclick="history.back()">back</button>
            </form>
        </div>
    </div>
</div>


</body>
</html>
