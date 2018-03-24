<<<<<<< HEAD
function showPasswords() {
    var x = document.getElementById("password");
    var x2 = document.getElementById("confirm_password");
    if (x.type === "password") {
        x.type = "text";
        x2.type = "text";
    } else {
        x.type = "password";
        x2.type = "password";
    }
}

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

/*$(document).ready(function(){
    if(getRequest().getSession().getAttribute("loggedInId") == null) {
        $('login').hide();
        $('register').show();
        $('account').hide();
        $('logout').hide();
    }else{
        $('login').show();
        $('register').hide();
        $('account').show();
        $('logout').show()
    }
});

        $('#btnSaveUser').on('click', function(){$('password').setAttribute('type','text');
        $('#btnLoginUser').on('click', function(){$('password').setAttribute('type','text');

        var password = document.getElementById("password");
        var confirm_password = document.getElementById("confirm-password");

        $('#password').on('change', function(){validatePassword()});
        $('#password').on('keyup', function(){validatePassword()});

})*/
=======
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

>>>>>>> 642fa583b65fd829731dc4849fbb38d8140ebf3a
