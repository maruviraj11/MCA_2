<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Change Password</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/app.css">
</head>
<body class="page">
    <div class="shell" style="max-width:640px;">
        <div class="panel">
            <h2>First Login Password Change</h2>
            <p class="muted">After your first login, set a new secure password here to continue using the system.</p>
            <form method="post" action="<%=request.getContextPath()%>/user">
                <input type="hidden" name="action" value="changePassword">
                <div style="margin-bottom:14px;"><label>New Password</label><input type="password" name="newPassword" required></div>
                <button type="submit">Update Password</button>
            </form>
        </div>
    </div>
</body>
</html>


