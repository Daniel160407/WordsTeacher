let userId;
let previousWordsAmount = 1;
$("document").ready(async function start() {
    await getWords(false);

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
        await getWords(false);

        document.getElementById("word").value = "";
        document.getElementById("meaning").value = "";
    });
});

async function getWords(levelUpPermission) {
    await fetch('/wordsTeacher/logIn', {method: "GET"})
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(data => {
            userId = parseInt(data);
        });

    let response = await fetch(`/wordsTeacher/words?userId=${userId}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/text"
        },
    });

    const jsonArray = await response.json();
    let dataToDisplay = "";

    for (let i = 0; i < jsonArray.length; i++) {
        dataToDisplay += "<p class='words'>" + jsonArray[i].id + ": " + jsonArray[i].word + " - "
            + jsonArray[i].meaning + "<p/><input id='" + jsonArray[i].id + "' type='checkbox' class='checkBox'> <br/>";
    }

    const div = document.getElementById("content");
    div.innerHTML = dataToDisplay;

    response = await fetch(`/wordsTeacher/wordsCounter?userId=${userId}&droppedWords=false`, {method: "GET"});
    const wordsAmount = await response.json();

    document.getElementById("freeSlots").innerText = "Free slots remaining: " + (100 - wordsAmount);

    const response1 = await fetch(`/wordsTeacher/wordsCounter?userId=${userId}&droppedWords=true`, {method: "GET"});
    const droppedWordsAmount = await response1.json();

    console.log("Words amount: " + droppedWordsAmount);

    console.log("local: " + droppedWordsAmount);
    console.log("global: " + previousWordsAmount);
    if (droppedWordsAmount == 0 && previousWordsAmount != 0 && levelUpPermission) {
        const h2 = document.getElementById("title").innerText;

        if ((parseInt(h2[h2.length - 1]) + 1) <= 5) {
            document.getElementById("title").innerText = "Level " + (parseInt(h2[h2.length - 1]) + 1);
            previousWordsAmount = 1;
        } else {
            previousWordsAmount = droppedWordsAmount;
            console.log("Recursion");
            document.getElementById("title").innerText = "Level 1";

            await fetch(`/wordsTeacher/words?userId=${userId}`, {method: "DELETE"});
            await getWords(false);
        }
    } else {
        previousWordsAmount = droppedWordsAmount;
    }
}

async function sendWords() {
    const checkboxes = document.querySelectorAll('input[type="checkbox"]:checked');
    const checkedCheckboxes = Array.from(checkboxes);

    let response = await fetch(`/wordsTeacher/words?userId=${userId}`, {method: "GET"});
    let jsonArray = await response.json();

    const words = [];

    for (let i = 0; i < jsonArray.length; i++) {
        for (let j = 0; j < checkedCheckboxes.length; j++) {
            if (jsonArray[i].id == checkedCheckboxes[j].id) {
                words.push(jsonArray[i]);
            }
        }
    }

    await fetch(`/wordsTeacher/wordDropper?userId=${userId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(words),
    });

    await getWords(true);
}