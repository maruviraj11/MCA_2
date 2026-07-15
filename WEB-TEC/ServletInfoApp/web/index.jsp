<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Servlet Information App</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f9;
            margin: 0;
            padding: 0;
        }

        .container {
            width: 450px;
            margin: 100px auto;
            background-color: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0px 5px 15px rgba(0,0,0,0.1);
            text-align: center;
        }

        h2 {
            color: #333;
            margin-bottom: 10px;
        }

        p {
            color: #555;
        }

        ul {
            text-align: left;
            margin-bottom: 20px;
        }

        li {
            margin: 5px 0;
        }

        input[type=submit] {
            padding: 8px 15px;
            background-color: #007bff;
            border: none;
            color: white;
            border-radius: 5px;
            cursor: pointer;
        }

        input[type=submit]:hover {
            background-color: #0056b3;
        }
    </style>

</head>

<body>

    <div class="container">
        <h2>Servlet Information Application</h2>

        <p>Click the button below to view:</p>

        <ul>
            <li>Init Parameters</li>
            <li>HTTP Request Headers</li>
            <li>Client / Browser Details</li>
            <li>Server Details</li>
        </ul>

        <form action="info" method="get">
            <input type="submit" value="Show Servlet Information">
        </form>
    </div>

</body>
</html>
