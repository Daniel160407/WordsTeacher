$("document").ready(async function start() {
    await getWords();

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

        await new Promise(resolve => setTimeout(resolve, 50));
        await getWords();
    });
});

async function getWords() {
    const url = "/wordsTeacher/words";
    const response = await fetch(url, {method: "GET"});

    const jsonArray = await response.json();
    let dataToDisplay = "";

    for (let i = 0; i < jsonArray.length; i++) {
        dataToDisplay += "<p class='words'>" + jsonArray[i].id + ": " + jsonArray[i].word + " - "
            + jsonArray[i].meaning + "<p/><input id='" + jsonArray[i].id + "' type='checkbox' class='checkBox'> <br/>";
    }
    const div = document.getElementById("content");
    div.innerHTML = dataToDisplay;

    const response1 = await fetch("/wordsTeacher/wordsCounter", {method: "GET"});
    const wordsAmount = await response1.json();
    document.getElementById("freeSlots").innerText = "Free slots remaining: " + (100 - wordsAmount);

    if (wordsAmount == 100) {
        const h2 = document.getElementById("title").innerText;
        document.getElementById("title").innerText = "Level " + (parseInt(h2[h2.length - 1]) + 1);
    }
}

async function sendWords() {
    const checkboxes = document.querySelectorAll('input[type="checkbox"]:checked');
    const checkedCheckboxes = Array.from(checkboxes);

    let response = await fetch("/wordsTeacher/words", {method: "GET"});
    let jsonArray = await response.json();

    const words = [];

    for (let i = 0; i < jsonArray.length; i++) {
        for (let j = 0; j < checkedCheckboxes.length; j++) {
            if (jsonArray[i].id == checkedCheckboxes[j].id) {
                words.push(jsonArray[i]);
            }
        }
    }

    await fetch("/wordsTeacher/wordDropper", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(words),
    });

    await getWords();
}