<#ftl encoding="UTF-8">
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Thank you for registration ${name}</title>
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

        #a {
            text-decoration: none;
        }
    </style>
</head>
<body>
<div id="body">
    <div id="div">
        <h1 style="text-align: center">Регистрация</h1>
        <p>Привет, ${name}!</p>
        <p>Это регистрационная почта. Вы можете войти в систему и использовать приложение списка задач.</p>
        <p>С наилучшими пожеланиями, <br> Школа Нейропсихологии</p>
        <a id="a" href="https://пиляева.рф">НЕЙРОПСИХОЛОГ</a>
    </div>
</div>
</body>
</html>