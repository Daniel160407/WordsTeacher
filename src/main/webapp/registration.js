$(document).ready(function () {
    $("#register").submit(function (event) {
        event.preventDefault();

        $.ajax({
            url: "/wordsTeacher/register",
            type: "POST",
            data: $(this).serialize(),
            success: function () {
                window.location.href = "/wordsTeacher/logIn.html";
            },
            error: function () {
                const div = document.getElementById("submitDiv").innerHTML;
                const messageText = "<h6>This account already exists!</h6>";

                if (!div.includes(messageText)) {
                    document.getElementById("submitDiv").innerHTML = messageText + div;
                }
            }
        });
    });
});