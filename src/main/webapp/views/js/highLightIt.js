// Tabbed Menu
// let categoriesList = document.getElementById("category").getElementsByTagName("a");
let categories = document.getElementById("category");
let categoriesList = categories.getElementsByTagName("a");
let className = "w3-blue";

for (let i = 0; i < categoriesList.length; i++) {
    categoriesList[i].addEventListener("click", highLight);
}

function highLight() {
    for (let i = 0; i < categoriesList.length; i++) {
        categoriesList[i].classList.remove(className);
    }
    this.classList.add(className);
}

// function openMenu(evt) {
//     let i, tabLinks;
//
//     tabLinks = document.getElementsByClassName("tablink");
//     for (i = 0; i < tabLinks.length; i++) {
//
//         tabLinks[i].className = tabLinks[i].className.replace(" w3-light-blue", "");
//     }
//     evt.currentTarget.className += " w3-light-blue";
// }
