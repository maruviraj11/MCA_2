<!DOCTYPE html>
<html>
<head>
    <title>Registration Form</title>
    <script>
        function validateForm() {

            var fname = document.forms["regForm"]["fname"].value;
            var lname = document.forms["regForm"]["lname"].value;
            var password = document.forms["regForm"]["password"].value;
            var confirmPassword = document.forms["regForm"]["confirmPassword"].value;
            var email = document.forms["regForm"]["email"].value;
            var mobile = document.forms["regForm"]["mobile"].value;
            var address = document.forms["regForm"]["address"].value;
            var role = document.forms["regForm"]["role"].value;

            var namePattern = /^[A-Za-z]{6,}$/;
            var emailPattern = /^[^ ]+@[^ ]+\.[a-z]{2,3}$/;
            var mobilePattern = /^[0-9]{10}$/;

            if (!namePattern.test(fname)) {
                alert("First Name must contain only alphabets and minimum 6 characters.");
                return false;
            }

            if (!namePattern.test(lname)) {
                alert("Last Name must contain only alphabets and minimum 6 characters.");
                return false;
            }

            if (password.length < 6) {
                alert("Password must be at least 6 characters.");
                return false;
            }

            if (password !== confirmPassword) {
                alert("Password and Confirm Password must match.");
                return false;
            }

            if (!emailPattern.test(email)) {
                alert("Enter valid email address.");
                return false;
            }

            if (!mobilePattern.test(mobile)) {
                alert("Mobile number must be 10 digits.");
                return false;
            }

            if (address.trim() == "") {
                alert("Address cannot be empty.");
                return false;
            }

            if (role == "") {
                alert("Please select user role.");
                return false;
            }

            return true;
        }
    </script>
</head>
<body>

<h2>Registration Form</h2>

<form name="regForm" action="RegisterServlet" method="post" onsubmit="return validateForm()">

    First Name: <input type="text" name="fname"><br><br>
    Last Name: <input type="text" name="lname"><br><br>
    Password: <input type="password" name="password"><br><br>
    Confirm Password: <input type="password" name="confirmPassword"><br><br>
    Email: <input type="text" name="email"><br><br>
    Mobile: <input type="text" name="mobile"><br><br>
    Address: <textarea name="address"></textarea><br><br>

    User Role:
    <select name="role">
        <option value="">--Select--</option>
        <option value="Admin">Admin</option>
        <option value="Registered User">Registered User</option>
        <option value="Guest">Guest</option>
    </select><br><br>

    <input type="submit" value="Register">

</form>

</body>
</html>
