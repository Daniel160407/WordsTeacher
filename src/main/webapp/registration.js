$(document).ready(function () {
    $("#register").submit(function (event) {
        event.preventDefault();

        $.ajax({
            url: "/wordsTeacher/registration",
            type: "POST",
            data: $(this).serialize(),
            success: function () {
                fetch("/wordsTeacher/logIn.html");
            },
            error: function () {
                document.getElementById("submitDiv").innerHTML = "<h6>This account already exists!</h6>" + document.getElementById("submitDiv").innerHTML;
            }
        });
    });
});