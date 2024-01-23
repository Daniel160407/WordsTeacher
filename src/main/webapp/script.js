$("document").ready(async function register() {
    const url = "/wordsTeacher/nouns";
    const method = "GET"
    const response = await fetch(url, {method: "GET"});

    const jsonArray = await response.json();

    for (let i = 0; i < jsonArray.length; i++) {
        const dataToDisplay = jsonArray[i].id + ": " + jsonArray[i].word + " - " + jsonArray[i].meaning;
        const div = document.getElementById("part1");
        div.innerHTML = dataToDisplay;
    }
})

const letters = {
    a: "ა",
    b: "ბ",
    c: "ც",
    d: "დ",
    e: "ე",
    f: "ფ",
    g: "გ",
    h: "ჰ",
    i: "ი",
    j: "ჯ",
    k: "კ",
    l: "ლ",
    m: "მ",
    n: "ნ",
    o: "ო",
    p: "პ",
    q: "ქ",
    r: "რ",
    s: "ს",
    t: "თ",
    u: "უ",
    v: "ვ",
    w: "წ",
    x: "ხ",
    y: "ყ",
    z: "ზ",
    dz: "ძ",
    ch: "ჩ",
    sh: "შ",
}