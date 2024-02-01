$(document).ready(function () {
    $("#logIn").submit(function (event) {
        event.preventDefault();

        $.ajax({
            url: "/wordsTeacher/logIn",
            type: "POST",
            data: $(this).serialize(),
            success: function () {
                fetch("/wordsTeacher/index.html");
            },
            error: function () {
                document.getElementById("submitDiv").innerHTML = "<h6>This account is not registered!</h6>" + document.getElementById("submitDiv").innerHTML;
            }
        });
    });
});