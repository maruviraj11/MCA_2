<%-- 
    Document   : index
    Created on : 2 Apr, 2026, 11:18:37 AM
    Author     : VIRAJ
--%>

<%@page   language="java" contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Calculator Form</title>
        
<script>
function validateForm()
{
    let op1 = document.forms["calcForm"]["operand1"].value;
    let op2 = document.forms["calcForm"]["operand2"].value;
    let operation = document.forms["calcForm"]["operation"];

    
    if(op1 == "")
    {
        alert("Operand 1 should not be blank");
        return false;
    }

   
    if(op2 == "")
    {
        alert("Operand 2 should not be blank");
        return false;
    }

   
    if(isNaN(op1) || isNaN(op2))
    {
        alert("Enter numeric values only");
        return false;
    }

    
    let selected = false;

    for(let i = 0; i < operation.length; i++)
    {
        if(operation[i].checked)
        {
            selected = true;

            
            if(operation[i].value == "div" && op2 == 0)
            {
                alert("Division by zero is not allowed");
                return false;
            }
        }
    }

    
    if(!selected)
    {
        alert("Please select an operation");
        return false;
    }

    return true;
}


function onlyNumber(event)
{
    let ch = event.key;

    
    if(ch === " " || ch === "+" || ch === "-" || ch === "e" || ch === "E" ||
       ch === "." || ch === "," || ch === "/" || ch === "*" || ch === "%" ||
       ch === "#" || ch === "@" || ch === "!" )
    {
        alert("Only digits allowed, special symbols not allowed!");
        event.preventDefault();
        return false;
    }

    
    if(ch < '0' || ch > '9')
    {
        event.preventDefault();
        return false;
    }
}
</script>

    </head>
    <body>
        <h1>Calculator Form</h1>
        <form name="calcForm" action="result.jsp" method="post" onsubmit="return validateForm()">
            <table border="1"> 
                <tr> 
                    <td>Operand 1 :</td> 
                    <td><input type="number" name="operand1" onkeypress="return noSpace(event)"  onkeypress="onlyNumber(event)"></td>
                </tr>
                <tr> 
                    <td>Operation :</td> 
                <td>
                    <input type="radio" name="operation" value="add"> Add <br>
                    <input type="radio" name="operation" value="sub"> Sub <br>
                    <input type="radio" name="operation" value="mul"> Mul <br> 
                    <input type="radio" name="operation" value="div"> Div 
                </td> 
                <tr> 
                    <td>Operand 2 :</td> 
                    <td>
                        <input type="number" name="operand2" onkeypress="return noSpace(event)" onkeypress="onlyNumber(event)">
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="submit" value="OK">
                    </td> 
                    <td>
                        <input type="reset" value="Reset">
                    </td> 
                </tr>
            </table>
        </form>
    </body>
</html>
