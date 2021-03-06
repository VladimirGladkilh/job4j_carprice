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
    <!--link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"-->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

    <style>
        * {
            box-sizing: border-box
        }

        body {
            font-family: Verdana, sans-serif;
            margin: 0
        }

        .mySlides {
            display: none
        }

        img {
            vertical-align: middle;
        }

        /* Slideshow container */
        .slideshow-container {
            max-width: 1000px;
            position: relative;
            margin: auto;
        }

        /* Next & previous buttons */
        .prev, .next {
            cursor: pointer;
            position: absolute;
            top: 50%;
            width: auto;
            padding: 16px;
            margin-top: -22px;
            color: white;
            font-weight: bold;
            font-size: 18px;
            transition: 0.6s ease;
            border-radius: 0 3px 3px 0;
            user-select: none;
        }

        /* Position the "next button" to the right */
        .next {
            right: 0;
            border-radius: 3px 0 0 3px;
        }

        /* On hover, add a black background color with a little bit see-through */
        .prev:hover, .next:hover {
            background-color: rgba(0, 0, 0, 0.8);
        }


        /* Number text (1/3 etc) */
        .numbertext {
            color: #f2f2f2;
            font-size: 12px;
            padding: 8px 12px;
            position: absolute;
            top: 0;
        }

        /* The dots/bullets/indicators */
        .dot {
            cursor: pointer;
            height: 15px;
            width: 15px;
            margin: 0 2px;
            background-color: #bbb;
            border-radius: 50%;
            display: inline-block;
            transition: background-color 0.6s ease;
        }


        /* Fading animation */
        .fade {
            -webkit-animation-name: fade;
            -webkit-animation-duration: 1.5s;
            animation-name: fade;
            animation-duration: 1.5s;
        }

        @-webkit-keyframes fade {
            from {
                opacity: .4
            }
            to {
                opacity: 1
            }
        }

        @keyframes fade {
            from {
                opacity: .4
            }
            to {
                opacity: 1
            }
        }

        /* On smaller screens, decrease text size */
        @media only screen and (max-width: 300px) {
            .prev, .next, .text {
                font-size: 11px
            }
        }
    </style>
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
                <!-- Slideshow container -->
                <div class="slideshow-container">
                    <% if (car.viewMainPhoto() == null) {%>
                    <div class="mySlides fade">
                        <div class="numbertext">Фото 0/0</div>
                        <img src="<%=request.getContextPath()%>/download?path=imgDefault.png" height="100px"
                             width="100%"/>
                    </div>
                    <%} else {%>
                    <% int i = 1;
                        for (Photo phot : car.getPhotos()
                        ) { %>
                    <div class="mySlides fade">
                        <div class="numbertext">Фото <%= i++ %> / <%= car.getPhotos().size()%>
                        </div>
                        <img src="<%=request.getContextPath()%>/download?path=<%=phot.getPath()%>"
                             height="300px" width="100%">
                    </div>
                    <% } %>
                    <!-- Next and previous buttons -->
                    <a class="prev" onclick="plusSlides(-1)">&#10094;</a>
                    <a class="next" onclick="plusSlides(1)">&#10095;</a>
                    <%}%>
                </div>
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
<script>
    var slideIndex = 1;
    showSlides(slideIndex);

    // Next/previous controls
    function plusSlides(n) {
        showSlides(slideIndex += n);
    }

    // Thumbnail image controls
    function currentSlide(n) {
        showSlides(slideIndex = n);
    }

    function showSlides(n) {
        var i;
        var slides = document.getElementsByClassName("mySlides");
        if (n > slides.length) {
            slideIndex = 1
        }
        if (n < 1) {
            slideIndex = slides.length
        }
        for (i = 0; i < slides.length; i++) {
            slides[i].style.display = "none";
        }
        slides[slideIndex - 1].style.display = "block";
    }
</script>

</body>
</html>
