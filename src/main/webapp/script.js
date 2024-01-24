$("document").ready(async function start() {
    getWords();

    $("#wordInputForm").submit(async function (event) {
        event.preventDefault();
        $.ajax({
            url: "/wordsTeacher/words",
            type: "POST",
            data: $(this).serialize(),
            success: function (data) {
                document.getElementById("freeSlots").innerText = "Free slots remaining: " + (100 - data);
            },
            error: function (jqXHR) {
                alert("Couldn`t save a word, limit reached! Error: " + jqXHR.status);
            }
        });

        await new Promise(resolve => setTimeout(resolve, 1000));
        await getWords();
    });
});

async function getWords() {
    const url = "/wordsTeacher/words";
    const response = await fetch(url, {method: "GET"});

    const jsonArray = await response.json();
    let dataToDisplay = "";
    console.log(jsonArray.length);
    for (let i = 0; i < jsonArray.length; i++) {
        dataToDisplay += "<p>" + jsonArray[i].id + ": " + jsonArray[i].word + " - " + jsonArray[i].meaning + "<p/><br/>";
    }
    const div = document.getElementById("part1Content");
    div.innerHTML = dataToDisplay;

    document.getElementById("freeSlots").innerText = "Free slots remaining: " + (100 - jsonArray.length);
}