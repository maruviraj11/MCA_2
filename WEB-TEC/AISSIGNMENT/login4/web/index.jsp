<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Login Page</title>

<style>
body{
    font-family: Arial;
    background: linear-gradient(to right,#4facfe,#00f2fe);
}
.login-box{
    width:350px;
    margin:100px auto;
    padding:30px;
    background:white;
    border-radius:10px;
    box-shadow:0 0 15px gray;
}
input{
    width:100%;
    padding:10px;
    margin:10px 0;
}
button{
    width:100%;
    padding:10px;
    background:#4facfe;
    color:white;
    border:none;
}
</style>

<script>
    function noSpace(event){
    
    if(event.key === " "){
        alert("Space is not allowed");
        event.preventDefault();
    }

}
function validateForm(){

    let email=document.getElementById("email").value.trim();
    let password=document.getElementById("password").value.trim();
    let emailPattern=/^[^ ]+@[^ ]+\.[a-z]{2,3}$/;
    let spacePattern=/\s/;

    if(email===""){
        alert("Email required");
        return false;
    }
     if(!email.match(emailPattern)){
        alert("Enter valid email format (example@gmail.com)");
        return false;
    }
     if(spacePattern.test(email)){
        alert("Spaces are not allowed in Email");
        return false;
    }
    if(password===""){
        alert("Password required");
        return false;
    }
    if(password.length < 6){
        alert("Password must be minimum 6 characters");
        return false;
    }

    return true;
}
</script>

</head>

<body>

<div class="login-box">

<h2 align="center">Login</h2>

<form action="loginServlet" method="post" onsubmit="return validateForm()">

<!--<input type="text" id="email" name="email" placeholder="Enter Email" maxlength="30" >

<input type="password" id="password" name="password" placeholder="Enter Password" maxlength="20">-->

<input type="text" id="email" name="email" onkeypress="noSpace(event)" maxlength="30">

<input type="password" id="password" name="password" onkeypress="noSpace(event)" maxlength="20">
<button type="submit">Login</button>

</form>

</div>

</body>
</html>