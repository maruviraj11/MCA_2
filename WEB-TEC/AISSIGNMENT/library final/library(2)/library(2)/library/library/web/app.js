(function () {
    function isPostForm(form) {
        var method = (form.getAttribute("method") || "").toLowerCase();
        return method === "post";
    }

    function shouldPreserve(form) {
        return form.hasAttribute("data-preserve") || form.classList.contains("no-auto-clear");
    }

    function setResetFlag() {
        try {
            sessionStorage.setItem("resetPostFormsNextLoad", "1");
        } catch (e) {
            // Ignore storage errors
        }
    }

    function consumeResetFlag() {
        try {
            var value = sessionStorage.getItem("resetPostFormsNextLoad");
            sessionStorage.removeItem("resetPostFormsNextLoad");
            return value === "1";
        } catch (e) {
            return false;
        }
    }

    function resetPostFormsIfNeeded() {
        if (!consumeResetFlag()) return;

        var forms = document.getElementsByTagName("form");
        for (var i = 0; i < forms.length; i++) {
            var form = forms[i];
            if (!isPostForm(form)) continue;
            if (shouldPreserve(form)) continue;
            if (typeof form.reset === "function") form.reset();
        }
    }

    function trim(value) {
        return (value || "").replace(/^\s+|\s+$/g, "");
    }

    function isEmpty(value) {
        return trim(value) === "";
    }

    function isValidEmail(email) {
        // Simple, practical email validation for client-side checks
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    }

    function isAlphaSpace(value) {
        return /^[A-Za-z ]+$/.test(value);
    }

    function isPositiveIntegerString(value) {
        return /^[0-9]+$/.test(value) && parseInt(value, 10) > 0;
    }

    function setValidity(input, message) {
        if (!input || typeof input.setCustomValidity !== "function") return;
        input.setCustomValidity(message || "");
    }

    function reportFirstInvalid(form) {
        if (!form) return;
        if (typeof form.reportValidity === "function") {
            form.reportValidity();
            return;
        }
        // Fallback: focus first invalid input
        var invalid = form.querySelector(":invalid");
        if (invalid && typeof invalid.focus === "function") invalid.focus();
    }

    function attachClearOnInput(input) {
        if (!input || input.__hasLiveValidityClear) return;
        input.__hasLiveValidityClear = true;
        input.addEventListener("input", function () {
            setValidity(input, "");
        });
        input.addEventListener("change", function () {
            setValidity(input, "");
        });
    }

    function passwordStrengthError(pass, minLen) {
        var value = pass || "";
        if (value.length < minLen) return "Password must be at least " + minLen + " characters long.";
        if (!/[A-Z]/.test(value)) return "Password must contain at least 1 uppercase letter.";
        if (!/[a-z]/.test(value)) return "Password must contain at least 1 lowercase letter.";
        if (!/[0-9]/.test(value)) return "Password must contain at least 1 number.";
        // Special character is recommended (optional), so not enforced here.
        return "";
    }

    function getActionValue(form) {
        var actionInput = form ? form.querySelector('input[name="action"]') : null;
        return actionInput ? trim(actionInput.value) : "";
    }

    function getInput(form, nameOrId) {
        if (!form) return null;
        return form.querySelector('[name="' + nameOrId + '"]') || document.getElementById(nameOrId);
    }

    function validateLogin(form) {
        var email = getInput(form, "email");
        var pass = getInput(form, "pass");
        if (email) {
            attachClearOnInput(email);
            var ev = trim(email.value);
            if (isEmpty(ev)) setValidity(email, "Email is required.");
            else if (!isValidEmail(ev)) setValidity(email, "Enter a valid email address (example: abc@gmail.com).");
            else setValidity(email, "");
        }
        if (pass) {
            attachClearOnInput(pass);
            if (isEmpty(pass.value)) setValidity(pass, "Password is required.");
            else setValidity(pass, "");
        }

        return form.checkValidity();
    }

    function validateRegister(form) {
        var email = getInput(form, "email");
        var pass = getInput(form, "pass");
        var cpass = getInput(form, "cpass");
        if (email) {
            attachClearOnInput(email);
            var ev = trim(email.value).toLowerCase();
            if (isEmpty(ev)) setValidity(email, "Email is required.");
            else if (!isValidEmail(ev)) setValidity(email, "Enter a valid email address (example: abc@gmail.com).");
            else setValidity(email, "");
            email.value = ev;
        }
        if (pass) {
            attachClearOnInput(pass);
            var strength = passwordStrengthError(pass.value, 8);
            setValidity(pass, strength);
        }
        if (cpass) {
            attachClearOnInput(cpass);
            if (isEmpty(cpass.value)) setValidity(cpass, "Confirm password is required.");
            else if (pass && cpass.value !== pass.value) setValidity(cpass, "Password and confirm password must match.");
            else setValidity(cpass, "");
        }

        return form.checkValidity();
    }

    function validateChangePassword(form) {
        var currentPass = getInput(form, "currentPass");
        var newPass = getInput(form, "newPass");
        var confirmPass = getInput(form, "confirmPass");

        if (currentPass) {
            attachClearOnInput(currentPass);
            if (isEmpty(currentPass.value)) setValidity(currentPass, "Old password is required.");
            else setValidity(currentPass, "");
        }

        if (newPass) {
            attachClearOnInput(newPass);
            var err = passwordStrengthError(newPass.value, 8);
            if (!err && currentPass && newPass.value === currentPass.value) {
                err = "New password must be different from old password.";
            }
            setValidity(newPass, err);
        }

        if (confirmPass) {
            attachClearOnInput(confirmPass);
            if (isEmpty(confirmPass.value)) setValidity(confirmPass, "Confirm new password is required.");
            else if (newPass && confirmPass.value !== newPass.value) setValidity(confirmPass, "New password and confirm password must match.");
            else setValidity(confirmPass, "");
        }

        return form.checkValidity();
    }

    function validateAdminAddAccount(form, emailName, passName) {
        var email = getInput(form, emailName || "email");
        var pass = getInput(form, passName || "pass");
        if (email) {
            attachClearOnInput(email);
            var ev = trim(email.value).toLowerCase();
            if (isEmpty(ev)) setValidity(email, "Email is required.");
            else if (!isValidEmail(ev)) setValidity(email, "Enter a valid email address.");
            else setValidity(email, "");
            email.value = ev;
        }
        if (pass) {
            attachClearOnInput(pass);
            var strength = passwordStrengthError(pass.value, 8);
            setValidity(pass, strength);
        }
        return form.checkValidity();
    }

    function validateAdminRemoveById(form, idName, label) {
        var input = getInput(form, idName);
        if (input) {
            attachClearOnInput(input);
            var value = trim(input.value);
            if (isEmpty(value)) setValidity(input, (label || "ID") + " is required.");
            else if (!isPositiveIntegerString(value)) setValidity(input, (label || "ID") + " must be a positive number.");
            else setValidity(input, "");
        }
        return form.checkValidity();
    }

    function validateAdminEdit(form, idName, emailName, passName, label) {
        var idInput = getInput(form, idName);
        var emailInput = getInput(form, emailName);
        var passInput = getInput(form, passName);

        if (idInput) {
            attachClearOnInput(idInput);
            var idValue = trim(idInput.value);
            if (isEmpty(idValue)) setValidity(idInput, (label || "ID") + " is required.");
            else if (!isPositiveIntegerString(idValue)) setValidity(idInput, (label || "ID") + " must be a positive number.");
            else setValidity(idInput, "");
        }

        var emailProvided = false;
        if (emailInput) {
            attachClearOnInput(emailInput);
            var emailValue = trim(emailInput.value);
            emailProvided = !isEmpty(emailValue);
            if (emailProvided) {
                emailValue = emailValue.toLowerCase();
                if (!isValidEmail(emailValue)) setValidity(emailInput, "Enter a valid email address.");
                else setValidity(emailInput, "");
                emailInput.value = emailValue;
            } else {
                setValidity(emailInput, "");
            }
        }

        var passProvided = false;
        if (passInput) {
            attachClearOnInput(passInput);
            var passValue = passInput.value || "";
            passProvided = !isEmpty(passValue);
            if (passProvided) {
                setValidity(passInput, passwordStrengthError(passValue, 8));
            } else {
                setValidity(passInput, "");
            }
        }

        // Require at least one change field (email or password)
        if ((!emailProvided && !passProvided) && (emailInput || passInput)) {
            if (emailInput) setValidity(emailInput, "Provide a new email or a new password to update.");
            else if (passInput) setValidity(passInput, "Provide a new email or a new password to update.");
        }

        return form.checkValidity();
    }

    function validateAddBook(form) {
        var title = getInput(form, "title");
        var author = getInput(form, "author");
        var category = getInput(form, "category");

        if (title) {
            attachClearOnInput(title);
            if (isEmpty(title.value)) setValidity(title, "Book title is required.");
            else setValidity(title, "");
        }

        if (author) {
            attachClearOnInput(author);
            var av = trim(author.value);
            if (isEmpty(av)) setValidity(author, "Author name is required.");
            else if (!isAlphaSpace(av)) setValidity(author, "Author name must contain only alphabets and spaces.");
            else setValidity(author, "");
            author.value = av;
        }

        if (category) {
            attachClearOnInput(category);
            var cv = trim(category.value);
            if (isEmpty(cv)) setValidity(category, "Category is required.");
            else setValidity(category, "");
            category.value = cv;
        }

        return form.checkValidity();
    }

    function validateBookCopy(form, fieldName, label) {
        var count = getInput(form, fieldName);
        if (count) {
            attachClearOnInput(count);
            var cv = trim(count.value);
            if (isEmpty(cv)) setValidity(count, (label || "Quantity") + " is required.");
            else if (!isPositiveIntegerString(cv)) setValidity(count, (label || "Quantity") + " must be an integer greater than or equal to 1.");
            else {
                var max = count.getAttribute("max");
                if (max && isPositiveIntegerString(max) && parseInt(cv, 10) > parseInt(max, 10)) {
                    setValidity(count, (label || "Quantity") + " cannot be greater than " + max + ".");
                } else {
                    setValidity(count, "");
                }
            }
        }
        return form.checkValidity();
    }

    function validateLibrarianRequest(form) {
        var statusInput = getInput(form, "status");
        var dueDateInput = getInput(form, "dueDate");
        var status = statusInput ? trim(statusInput.value).toLowerCase() : "";

        if (dueDateInput) {
            attachClearOnInput(dueDateInput);
            if (status === "issued") {
                var dueValue = trim(dueDateInput.value);
                if (isEmpty(dueValue)) {
                    setValidity(dueDateInput, "Due date is required before approving request.");
                } else {
                    var today = new Date();
                    today.setHours(0, 0, 0, 0);
                    var dueDate = new Date(dueValue + "T00:00:00");
                    if (dueDate < today) setValidity(dueDateInput, "Due date cannot be before today.");
                    else setValidity(dueDateInput, "");
                }
            } else {
                setValidity(dueDateInput, "");
            }
        }

        return form.checkValidity();
    }

    function validateSearch(form, queryName, allowEmptyIfOtherFilled) {
        var query = getInput(form, queryName || "searchQuery");
        var sort = getInput(form, "sortBy");

        if (query) {
            attachClearOnInput(query);
            var qv = trim(query.value);
            var sortValue = sort ? trim(sort.value) : "";

            if (allowEmptyIfOtherFilled && isEmpty(qv) && !isEmpty(sortValue)) {
                setValidity(query, "");
                return true;
            }

            if (isEmpty(qv) && isEmpty(sortValue)) {
                setValidity(query, "Search field cannot be empty.");
            } else if (isEmpty(qv) && !isEmpty(sortValue)) {
                setValidity(query, "");
            } else {
                // Optional: strip special characters (keep letters, numbers, spaces)
                // This makes search safer/nicer without blocking user input.
                query.value = qv.replace(/[^A-Za-z0-9 ]+/g, "");
                setValidity(query, "");
            }
        }

        return form.checkValidity();
    }

    function validateFormsOnSubmit(event) {
        var form = event.target;
        if (!form || form.tagName !== "FORM") return;

        var action = trim(form.getAttribute("action") || "");
        var method = (form.getAttribute("method") || "GET").toUpperCase();
        var hiddenAction = getActionValue(form);
        var valid = true;

        // Auth
        if (action === "LoginServlet" && method === "POST") valid = validateLogin(form);
        else if (action === "RegisterServlet" && method === "POST") valid = validateRegister(form);

        // Profile
        else if (action === "ProfileServlet" && hiddenAction === "changePassword" && method === "POST") valid = validateChangePassword(form);

        // Admin
        else if (action === "AdminServlet" && hiddenAction === "addUser" && method === "POST") valid = validateAdminAddAccount(form, "email", "pass");
        else if (action === "AdminServlet" && hiddenAction === "addLibrarian" && method === "POST") valid = validateAdminAddAccount(form, "email", "pass");
        else if (action === "AdminServlet" && hiddenAction === "removeUser" && method === "POST") valid = validateAdminRemoveById(form, "userId", "User ID");
        else if (action === "AdminServlet" && hiddenAction === "removeLibrarian" && method === "POST") valid = validateAdminRemoveById(form, "librarianId", "Librarian ID");
        else if (action === "AdminServlet" && hiddenAction === "editUser" && method === "POST") valid = validateAdminEdit(form, "userId", "email", "pass", "User ID");
        else if (action === "AdminServlet" && hiddenAction === "editLibrarian" && method === "POST") valid = validateAdminEdit(form, "librarianId", "email", "pass", "Librarian ID");

        // Librarian inventory
        else if (action === "LibrarianServlet" && hiddenAction === "addBook" && method === "POST") valid = validateAddBook(form);
        else if (action === "LibrarianServlet" && hiddenAction === "editBook" && method === "POST") {
            valid = validateAdminRemoveById(form, "bookId", "Book ID") && validateAddBook(form);
        }
        else if (action === "LibrarianServlet" && hiddenAction === "addBookCopy" && method === "POST") valid = validateBookCopy(form, "addCount", "Quantity");
        else if (action === "LibrarianServlet" && hiddenAction === "removeBookCopy" && method === "POST") valid = validateBookCopy(form, "removeCount", "Quantity");
        else if (action === "LibrarianServlet" && hiddenAction === "updateRequest" && method === "POST") valid = validateLibrarianRequest(form);

        // Search / Sort panels
        else if (action === "librarian_books.jsp" && method === "GET") valid = validateSearch(form, "searchQuery", false);
        else if (action === "user_books.jsp" && method === "GET") valid = validateSearch(form, "searchQuery", true);

        if (!valid) {
            event.preventDefault();
            // Prevent other submit handlers (like "auto-clear post forms") from running.
            if (typeof event.stopImmediatePropagation === "function") event.stopImmediatePropagation();
            else if (typeof event.stopPropagation === "function") event.stopPropagation();
            reportFirstInvalid(form);
        }
    }

    function resetPostFormFlagOnSubmit(event) {
        var target = event.target;
        if (!target || target.tagName !== "FORM") return;
        if (!isPostForm(target)) return;
        if (shouldPreserve(target)) return;
        setResetFlag();
    }

    function confirmLibrarianAction(event) {
        var form = event.target;
        if (!form || form.tagName !== "FORM") return;
        if ((trim(form.getAttribute("action") || "")) !== "LibrarianServlet") return;

        var actionType = trim(form.getAttribute("data-request-action") || "");
        if (!actionType) return;

        var message = "";
        if (actionType === "approve") message = "Approve this book request?";
        else if (actionType === "reject") message = "Reject this book request?";
        else if (actionType === "return") message = "Mark this book as returned?";
        else if (actionType === "finePaid") message = "Mark this fine as paid?";

        if (message && !window.confirm(message)) {
            event.preventDefault();
            if (typeof event.stopImmediatePropagation === "function") event.stopImmediatePropagation();
            else if (typeof event.stopPropagation === "function") event.stopPropagation();
        }
    }

    // Run validations before we do any post-submit auto-reset flagging.
    document.addEventListener("submit", validateFormsOnSubmit, true);
    document.addEventListener("submit", confirmLibrarianAction, true);
    document.addEventListener("submit", resetPostFormFlagOnSubmit, true);

    document.addEventListener("DOMContentLoaded", resetPostFormsIfNeeded);
})();
