document.addEventListener("DOMContentLoaded", loadUser);

async function loadUser() {
    const response = await fetch("/api/user");

    if (!response.ok) {
        alert("Could not load user information");
        return;
    }

    const user = await response.json();
    renderUser(user);
}

function renderUser(user) {
    const tableBody = document.getElementById("user-table-body");
    tableBody.innerHTML = "";

    const row = document.createElement("tr");

    const values = [
        user.id,
        user.name,
        user.surname,
        user.email,
        user.username,
        user.roles.join(", ")
    ];

    for (const value of values) {
        const cell = document.createElement("td");
        cell.textContent = value;
        row.appendChild(cell);
    }

    tableBody.appendChild(row);
}
