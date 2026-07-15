<%-- 
    Document   : index
    Created on : Feb 13, 2026, 11:50:45 AM
    Author     : bhila
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
        <title>Compound Interest</title>
        <style>
        *{
            margin:0;
            padding: 0;
        }
        body
        {
            background-color: lightblue;
           
        }
        .divDesign
        {
            align-items: center;
           
        }
        .Container
        {
            margin: auto;
            margin-top: 50px;
            background: ghostwhite;
            width: 520px;
            height: 450px;
            align-items: center;
            border-radius: 24px;
            border-style: inset;
            border-width: 7px;
        }
        </style>
    </head>
    <body>
    
        <div class="Container">
            <form method="post" action="CalculateServlet" onsubmit="return validateForm()">
                <div class="divDesign" style="margin-top: 10px"> <h1 style="color: purple" align="center">Calculator For Compound Interest</h1></div>
                <div class="main" style="margin-left: 10px; margin-top: 10px;">
                    
                <div class="row align-items-center mb-4">
                    <label class="col-sm-5 col-form-label text-center" style="font-size: 18px"> Principle Amount : </label>
                    <div class="col-sm-6">
                        <input type="number" id="principal" onchange="ValidPrincipal()" class="form-control" name="principal" placeholder="Enter Principle Amount" required>
                       </div>
                </div>
                    
                <div class="row align-items-center mb-4">
                    <label class="col-sm-5 col-form-label text-center" style="font-size: 18px"> Interest Rate : </label>
                    <div class="col-sm-6">
                        <input type="number" id="rate" class="form-control" name="rate" id="rate" placeholder="Enter Intrest Rate" name="rate" required>
                    </div>
                    <div class="col-sm-1" style="margin-left: -15px;">
                        <span class="input-group-text form-control"  id="basic-addon2">%</span>
                    </div>
                </div>
                    
                <div class="row align-items-center mb-4">
                    <label class="col-sm-5 col-form-label text-center" style="font-size: 18px"> Years: </label>
                    <div class="col-sm-6">
                        <input type="number" id="years" class="form-control" name="years" placeholder="Enter No. of Years" required>
                    </div>
                </div>
                    
                <div class="row align-items-center mb-4">
                    <label class="col-sm-5 col-form-label text-center" style="font-size: 18px"> Months: </label>
                    <div class="col-sm-6">
                        <input type="number" id="months" class="form-control" name="months" placeholder="Enter No. of Months" required>
                    </div>
                </div>
                
                 
                <div class="row align-items-center mb-4">
                    <label class="col-sm-5 col-form-label text-center" style="font-size: 18px"> Compound Interval </label>
                    <div class="col-sm-6">
                        <select id="interval" class="form-control" name="interval" style="width:150px; height: 25px;" required>
                            <option value="365">Daily (365/yr)</option>
                            <option value="360">Daily (360/yr)</option>
                            <option value="52">Weekly (52/yr)</option>
                            <option value="26">Bi-Weekly (26/yr)</option>
                            <option value="24">Half-Monthly (24/yr)</option>
                            <option value="12">Monthly (12/yr)</option>
                            <option value="6">Bi-Monthly (6/yr)</option>
                            <option value="4">Quarterly (4/yr)</option>
                            <option value="2">Half-Yearly (2/yr)</option>
                            <option value="1">Yearly (1/yr)</option>
                        </select>
                    </div>
                </div>

                <div class="span1" style="margin-top: 10px; font-size: 12px;">
                    <div style="margin-left: 150px;">
                        <input type="reset" value="Reset" class="btn btn-lg btn-outline-danger mt-3" style="margin-left: 7px;">
                        <input type="submit" value="Calculate"  class="btn btn-lg btn-outline-success mt-3" style="margin-left: 7px;">
                    </div>
                </div>
            </div>
            </form>
        </div>
        
         <script>
             
            function validateForm() {
                var amount = document.getElementById("principal").value;
                var rate = document.getElementById("rate").value;
                var year = document.getElementById("years").value;
                var months = document.getElementById("months").value;
                var calculateinterest = document.getElementById("interval").value;

                var number_pattern = /^[0-9]*$/;
                var float_pattern = /^\d*(\.\d{1,2})?$/;

                if (amount.trim() === "" || !number_pattern.test(amount)) {
                    alert("Enter a amount !");
                    return false;
                }

                if (rate.trim() === "" || !float_pattern.test(rate) || parseFloat(rate) > 100) {
                    alert("Enter a valid interest rate less than or equal to 100 !");
                     return false;
                    }

                if (year.trim() === "" || !number_pattern.test(year)) {
                    alert("Enter a valid number of year!.");
                    return false;
                }
//                if (parseInt(year) === 0 && parseInt(months) > 0) {
//                    alert("If the number of years is 0, the number of months should be greater than 0.");
//                        return false;
//                            }

                if (months.trim() === "" || !number_pattern.test(months) || parseInt(months) > 12) {
                    alert("Enter a valid number of months(not greater than  12).");
                    return false;
                }
                 if (year.trim() === "" || !number_pattern.test(year) || parseInt(year) > 50) {
                    alert("Enter a valid number of years(not greater than  50).");
                    return false;
                }

                if (calculateinterest === "") {
                    alert("Select a compound interval !");
                    return false;
                }
 
            }
        </script>
        
    </body>
</html>
