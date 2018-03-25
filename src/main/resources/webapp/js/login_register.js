document.addEventListener('DOMContentLoaded',
    function (){
        var test = document.getElementById('account').innerHTML;
        if( test) {
            document.getElementById('login').style.display = 'none';
            document.getElementById('register').style.display = 'none';
            document.getElementById('account').style.display = 'block';
            document.getElementById('account').insertAdjacentHTML("afterbegin","Hi! ");
            document.getElementById('logout').style.display = 'block';
        }else{
            document.getElementById('login').style.display = 'block';
            document.getElementById('register').style.display = 'block';
            document.getElementById('account').style.display = 'none';
            document.getElementById('logout').style.display = 'none';
        }
    }, false);



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


/*
document.getElementById('logout').addEventListener('click',
    function(){
    confirm("Are you sure you want to log out?");
    if (!"$userManager"){
        alert("you are successfully logged out")
    }
    }
    ,false
);
*/


