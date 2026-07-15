<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>Book Form</title>

<style>
body{
    font-family: Arial;
    background:#f2f2f2;
}

.container{
    width:400px;
    margin:auto;
    margin-top:50px;
    background:white;
    padding:20px;
    border-radius:10px;
    box-shadow:0px 0px 10px gray;
}

input{
    width:100%;
    padding:8px;
    margin-top:5px;
    margin-bottom:10px;
}

input[type=submit]{
    background:green;
    color:white;
    border:none;
}
</style>

<script>

function onlyText(event){
    let char = String.fromCharCode(event.which);

    if(!/[A-Za-z ]/.test(char)){
        event.preventDefault();
    }
}

function onlyNumber(event){
    let char = String.fromCharCode(event.which);

    if(!/[0-9]/.test(char)){
        event.preventDefault();
    }
}

function validateForm(){

    let title=document.forms["bookForm"]["title"].value;
    let author=document.forms["bookForm"]["author"].value;
    let isbn=document.forms["bookForm"]["isbn"].value;

    if(title.length < 3 || title.length > 30){
        alert("Title min 3 max 30 characters");
        return false;
    }

    if(author.length < 3 || author.length > 20){
        alert("Author min 3 max 20 characters");
        return false;
    }

    if(isbn.length < 5 || isbn.length > 13){
        alert("ISBN min 5 max 13 digits");
        return false;
    }

    return true;
}

</script>

</head>
<body>

<div class="container">

<h2>Book Form</h2>

<form name="bookForm" action="insert.jsp" method="post" onsubmit="return validateForm()">

Title:
<input type="text" name="title"
onkeypress="onlyText(event)"
minlength="3"
maxlength="30"
required>

Author:
<input type="text" name="author"
onkeypress="onlyText(event)"
minlength="3"
maxlength="20"
required>

Price:
<input type="text" name="price"
onkeypress="onlyNumber(event)"
maxlength="5"
required>

Quantity:
<input type="text" name="quantity"
onkeypress="onlyNumber(event)"
maxlength="3"
required>

ISBN:
<input type="text" name="isbn"
onkeypress="onlyNumber(event)"
minlength="5"
maxlength="13"
required>

Publisher:
<input type="text" name="publisher"
onkeypress="onlyText(event)"
maxlength="20"
required>

Edition Year:
<input type="text" name="year"
onkeypress="onlyNumber(event)"
maxlength="4"
required>

Catalogue Id:
<input type="text" name="catalogueId"
onkeypress="onlyNumber(event)"
maxlength="5"
required>

<input type="submit" value="Insert Book">

</form>

</div>

</body>
</html>