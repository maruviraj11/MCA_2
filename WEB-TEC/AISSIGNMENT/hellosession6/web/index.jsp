<%-- 
    Document   : index
    Created on : 12 Mar, 2026, 11:38:02 AM
    Author     :  viraj maru
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
        <script>
            
          function validateForm()
         {
             var name = document.getElementById("name").value.trim();

             if(name === "")
             {
                 alert("do not blank any filed");
                 return false;
             }

             return true;
         }
         function noSpace(event)
            {
                if(event.key === " ")
                {
                    alert("Space is not allowed");
                    return false;
                }
            }


            function onlyLetters(event)
            {
                var char = event.key;

                if(!/[a-zA-Z]/.test(char))
                {
                    alert("Only letters allowed in Name field");
                    return false;
                }
}

            </script>
            
    </head>
    <body>
       <h2>Enter Your Name</h2>

<form action="helloservlet" method="post"  onsubmit="return validateForm()">
<!--Name : <input type="text" id="name" name="name"  onkeypress="return noSpace(event)" onkeypress="return onlyLetters(event)" required>-->
<input type="text" name="name" id="name"
onkeypress="return onlyLetters(event)"  
onkeypress="return noSpace(event)"
maxlength="15" placeholder="ENTER YOUR NAME"><br>
<br><br>

<input type="submit" value="Submit">

</form>
    </body>
</html>
