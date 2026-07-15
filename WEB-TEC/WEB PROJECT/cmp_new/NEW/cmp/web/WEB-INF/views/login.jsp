<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Complaint Management Portal</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/app.css">
</head>
<body class="login-body">
    <div class="login-bg" aria-hidden="true">
        <span class="login-blob one"></span>
        <span class="login-blob two"></span>
        <span class="login-blob three"></span>
        <span class="login-grid"></span>
    </div>
    <div class="login-shell">
        <div class="login-card">
            <div class="login-showcase">
                <span class="badge warn">Campus Support System</span>
                <h1>Complaint Management Portal</h1>
                <p>One place to register complaints, track progress, and manage resolution with role-based access.</p>
                <div class="login-highlights">
                    <div class="login-highlight-card">
                        <span>Centralized</span>
                        <strong>Submit and manage complaints</strong>
                    </div>
                    <div class="login-highlight-card">
                        <span>Transparent</span>
                        <strong>Status updates in real time</strong>
                    </div>
                    <div class="login-highlight-card">
                        <span>Organized</span>
                        <strong>Role-wise dashboards for staff</strong>
                    </div>
                </div>
                <div class="login-admin-box">
                    <div class="login-admin-label">Tip</div>
                    <div style="font-weight:600;line-height:1.55;">
                        After your first login, change your temporary password from the dashboard.
                    </div>
                </div>
            </div>
            <div class="login-form">
                <div class="login-form-head">
                    <h2>Sign In</h2>
                    <p class="muted">Enter your email and password to open your role-based dashboard.</p>
                <% if (request.getAttribute("error") != null) { %>
                    <div class="message error"><%=request.getAttribute("error")%></div>
                <% } %>
                </div>
                <form id="loginForm" method="post" action="<%=request.getContextPath()%>/login">
                    <div class="login-field">
                        <label>Email Address</label>
                        <input id="emailField" type="email" name="email" placeholder="name@example.com" autocomplete="username" autocapitalize="none" spellcheck="false" required>
                    </div>
                    <div class="login-field">
                        <label>Password</label>
                        <div class="login-password">
                            <input id="passwordField" type="password" name="password" placeholder="Enter your password" autocomplete="current-password" required>
                            <button type="button" class="password-toggle" aria-label="Show password" onclick="togglePassword()">Show</button>
                        </div>
                    </div>
                    <button type="submit" class="login-submit">Login</button>
                </form>
                <div class="login-footer-note">
                    Use the email and password provided by the administrator. If this is your first login, please update your password immediately.
                </div>
            </div>
        </div>
    </div>

    <script>
        (function () {
            var form = document.getElementById("loginForm");
            var emailField = document.getElementById("emailField");
            if (!form || !emailField) return;

            function clearEmailError() {
                emailField.classList.remove("field-error");
                var existing = emailField.parentNode.querySelector(".validation-text");
                if (existing) {
                    existing.parentNode.removeChild(existing);
                }
            }

            function showEmailError(message) {
                emailField.classList.add("field-error");
                var node = document.createElement("div");
                node.className = "validation-text";
                node.textContent = message;
                emailField.parentNode.appendChild(node);
            }

            function normalizeEmail(value) {
                return (value || "").trim().toLowerCase();
            }

            function isValidEmail(value) {
                return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
            }

            form.addEventListener("submit", function (event) {
                clearEmailError();
                var normalized = normalizeEmail(emailField.value);
                emailField.value = normalized;
                if (!isValidEmail(normalized)) {
                    event.preventDefault();
                    showEmailError("Enter a valid email address.");
                }
            });
        })();

        function togglePassword() {
            var field = document.getElementById("passwordField");
            var btn = document.querySelector(".password-toggle");
            if (!field || !btn) return;
            var show = field.type === "password";
            field.type = show ? "text" : "password";
            btn.textContent = show ? "Hide" : "Show";
            btn.setAttribute("aria-label", show ? "Hide password" : "Show password");
        }
    </script>
</body>
</html>



