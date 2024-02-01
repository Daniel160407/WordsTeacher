$(document).ready(function () {
    $("#logIn").submit(function (event) {
        event.preventDefault();

        $.ajax({
            url: "/wordsTeacher/logIn",
            type: "POST",
            data: $(this).serialize(),
            success: function () {
                window.location.href = "/wordsTeacher/index.html";
            },
            error: function () {
                const div = document.getElementById("submitDiv").innerHTML;
                const messageText = "<h6>This account is not registered!</h6>";

                if (!div.includes(messageText)) {
                    document.getElementById("submitDiv").innerHTML = messageText + div;
                }
            }
        });
    });
});