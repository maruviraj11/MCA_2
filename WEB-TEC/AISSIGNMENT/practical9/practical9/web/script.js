/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */



function validateForm() {

    let title = document.forms[0]["title"].value;
    let author = document.forms[0]["author"].value;
    let price = document.forms[0]["price"].value;
    let quantity = document.forms[0]["quantity"].value;
    let isbn = document.forms[0]["isbn"].value;
    let year = document.forms[0]["year"].value;
    let qty = document.forms[0]["quantity"].value;
    let price = document.forms[0]["price"].value;


    // Title validation
    if(qty.length > 3) {
        alert("Quantity max 3 digits allowed!");
        return false;
    }

    // Price max 5 digit
    if(price.length > 5) {
        alert("Price too long!");
        return false;
    }

    return true;
    if (title.length < 3) {     
        alert("Title must be at least 3 characters!");
        return false;
    }

    // Author validation
    if (!/^[A-Za-z ]+$/.test(author)) {
        alert("Author must contain only letters!");
        return false;
    }

    // Price validation
    if (price <= 0) {
        alert("Price must be greater than 0!");
        return false;
    }

    // Quantity validation
    if (quantity < 1) {
        alert("Quantity must be at least 1!");
        return false;
    }

    // ISBN validation (13 digit)
    if (!/^[0-9]{10,13}$/.test(isbn)) {
        alert("ISBN must be 10 to 13 digits!");
        return false;
    }

    // Year validation
    if (year < 1900 || year > 2099) {
        alert("Enter valid year!");
        return false;
    }

    return true;
}
function onlyText(input) {
    let value = input.value;

    // allow only letters and space
    if (!/^[A-Za-z ]*$/.test(value)) {
        alert("Only letters allowed!");
        input.value = value.replace(/[^A-Za-z ]/g, '');
    }
}

function onlyNumber(input) {
    let value = input.value;

    // allow only digits
    if (!/^[0-9]*$/.test(value)) {
        alert("Only numbers allowed!");
        input.value = value.replace(/[^0-9]/g, '');
    }
}