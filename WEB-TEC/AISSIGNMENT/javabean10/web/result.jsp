<%-- 
    Document   : result
    Created on : 2 Apr, 2026, 11:44:26 AM
    Author     : VIRAJ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>RESULT PAGE</h1>
      
<%--<%@ page language="java" contentType="text/html;charset=UTF-8" %>--%>

<html>
<body>

<jsp:useBean id="calc" class="cal.CalculatorBean" scope="request"/>

<jsp:setProperty name="calc" property="operand1" param="operand1"/>
<jsp:setProperty name="calc" property="operand2" param="operand2"/>
<jsp:setProperty name="calc" property="operation" param="operation"/>

<h2>Result is : <jsp:getProperty name="calc" property="result"/></h2>




    </body>
</html>
