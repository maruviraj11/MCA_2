<%-- 
    Document   : index
    Created on : 15 Mar, 2026, 11:03:30 AM
    Author     : VIRAJ
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
                var name= document.getElementById("name").value.trime();
                
                if(name=="")
                {
                    alert("DO NOT BLANK FILEDS");
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
            </script>
    </head>
    <body>
        <h1>SESSION FORM</h1>
        
        <form action="hello" method="post"onsubmit="return validateForm()">
            <lable>NAME</lable><br>
            <input type="text" name="name" id="name" required onkeypress="return noSpace(event)"><br>
            
            <input type="submit" value="submit" ><br>
        </form>
    </body>
</html>
