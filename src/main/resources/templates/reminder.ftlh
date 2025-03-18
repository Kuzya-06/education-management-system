<#ftl encoding="UTF-8">
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>You have a task to do in 1 hour</title>
    <style>
        #body {
            display: flex;
            flex-direction: column;
            font-family: "Segoe UI", serif;
            align-items: center;
            background-color: #607FF2;
            font-size: 20px;
            height: 100%;
        }

        #div {
            display: block;
            flex-direction: column;
            margin: 10%;
            font-family: "Segoe UI", serif;
            background-color: white;
            padding: 3%;
            border-radius: 20px;
            color: black;
        }

        #button {
            background-color: #607FF2;
            border-radius: 10px;
            padding: 5px;
            color: white;
            text-decoration: none;
        }

        #a {
            text-decoration: none;
        }
    </style>
</head>
<body>
<div id="body">
    <div id="div">
        <h1 style="text-align: center">Напоминание</h1>
        <p>Привет, ${name}!</p>
        <p>Это дружеское напоминание о том, что у вас есть задача за 1 час.</p>
        <div>
            <hr>
            <p>Заголовок: ${title}</p>
            <#if description??>
                <p>Описание: ${description}</p>
            </#if>
            <hr>
        </div>
        <p>Вы можете получить больше информации об этой задаче в приложении.</p>
        <p>С наилучшими пожеланиями,  <br> Школа Нейропсихологии</p>
        <a id="a" href="https://пиляева.рф">НЕЙРОПСИХОЛОГ</a>
    </div>
</div>
</body>
</html>