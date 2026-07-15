<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Compound Interest Calculator</title>

<style>
    body {
        font-family: Arial, sans-serif;
        margin: 0;
        padding: 0;
        background: linear-gradient(to right, #87CEEB, #00BFFF);
    }

    .container {
        width: 400px;
        margin: 70px auto;
        padding: 30px;
        background-color: #ffffff;
        border-radius: 12px;
        box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
    }

    h2 {
        text-align: center;
        color: #0077b6;
        margin-bottom: 25px;
    }

    label {
        font-weight: bold;
        color: #023e8a;
        display: block;
        margin-top: 12px;
    }

    input[type="text"] {
        width: 100%;
        padding: 10px;
        margin-top: 6px;
        border-radius: 6px;
        border: 1px solid #90e0ef;
        outline: none;
        transition: 0.3s;
    }

    input[type="text"]:focus {
        border-color: #00b4d8;
        box-shadow: 0 0 5px #90e0ef;
    }

    input[type="submit"] {
        width: 100%;
        padding: 12px;
        margin-top: 20px;
        background: linear-gradient(to right, #00BFFF, #0077b6);
        color: white;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        font-size: 16px;
        transition: 0.3s;
    }

    input[type="submit"]:hover {
        background: linear-gradient(to right, #0077b6, #023e8a);
        transform: scale(1.03);
    }
</style>

<script>
function validateForm() {

    var principal = document.forms["ciForm"]["principal"].value;
    var rate = document.forms["ciForm"]["rate"].value;
    var compound = document.forms["ciForm"]["compound"].value;
    var months = document.forms["ciForm"]["months"].value;

    if (principal === "" || rate === "" || compound === "" || months === "") {
        alert("All fields are required!");
        return false;
    }

    if (isNaN(principal) || isNaN(rate) || isNaN(compound) || isNaN(months)) {
        alert("Please enter numeric values only!");
        return false;
    }

    if (principal <= 0 || rate <= 0 || compound <= 0 || months <= 0) {
        alert("Values must be greater than 0!");
        return false;
    }

    return true;
}
</script>

</head>

<body>

<div class="container">

<h2>Compound Interest Calculator</h2>

<form name="ciForm" action="CIServlet" method="post" onsubmit="return validateForm()">

    <label>Principal Amount (P):</label>
    <input type="text" name="principal">

    <label>Annual Interest Rate (%):</label>
    <input type="text" name="rate">

    <label>Compounded Per Year (n):</label>
    <input type="text" name="compound">

    <label>Total Months:</label>
    <input type="text" name="months">

    <input type="submit" value="Calculate">

</form>

</div>

</body>
</html>
