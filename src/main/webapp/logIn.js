$(document).ready(function () {
    $("#logIn").submit(function (event) {
        event.preventDefault();
        console.log("sent");
        $.ajax({
            url: "/wordsTeacher/logIn",
            type: "POST",
            data: $(this).serialize(),
            success: async function () {
                window.location.href = "/wordsTeacher/index.html";
            },
            error: function () {
                document.getElementById("submitDiv").innerHTML = "<h6>This account is not registered!</h6>" + document.getElementById("submitDiv").innerHTML;
            }
        });
    });
});