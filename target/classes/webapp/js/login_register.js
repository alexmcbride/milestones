function showPasswords() {
    var x = document.getElementById("password");
    var x2 = document.getElementById("confirm-password");
    if (x.type === "password") {
        x.type = "text";
        x2.type = "text";
    } else {
        x.type = "password";
        x2.type = "password";
    }
}

var password = document.getElementById("password");
var confirm_password = document.getElementById("confirm-password");



    var password = document.getElementById("password")
        , confirm_password = document.getElementById("confirm_password");

    function validatePassword(){
        if(password.value != confirm_password.value) {
            confirm_password.setCustomValidity("Passwords Don't Match");
        } else {
            confirm_password.setCustomValidity('');
        }
    }
    password.onchange = validatePassword;
    confirm_password.onkeyup = validatePassword;

