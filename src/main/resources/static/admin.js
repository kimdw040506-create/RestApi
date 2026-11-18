let users = [];
let roles = [];
let editingUserId = null;
let deletingUserId = null;

function getCsrfToken() {
    return document
        .querySelector('meta[name="_csrf"]')
        .getAttribute("content");
}

function getCsrfHeaderName() {
    return document
        .querySelector('meta[name="_csrf_header"]')
        .getAttribute("content");
}

const userModal = new bootstrap.Modal(
    document.getElementById("user-modal")
);

const deleteModal = new bootstrap.Modal(
    document.getElementById("delete-modal")
);

document.addEventListener("DOMContentLoaded", async function () {
    document.getElementById("add-user-button")
        .addEventListener("click", openCreateModal);

    document.getElementById("user-form")
        .addEventListener("submit", saveUser);

    document.getElementById("confirm-delete-button")
        .addEventListener("click", deleteUser);

    await loadRoles();
    await loadUsers();
});

async function loadUsers() {
    const response = await fetch("/api/admin/users");

    if (!response.ok) {
        alert("Could not load users");
        return;
    }

    users = await response.json();
    renderUsers();
}

async function loadRoles() {
    const response = await fetch("/api/admin/roles");

    if (!response.ok) {
        alert("Could not load roles");
        return;
    }

    roles = await response.json();
}

function renderUsers() {
    const tableBody = document.getElementById("users-table-body");
    tableBody.innerHTML = "";

    for (const user of users) {
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

        const editCell = document.createElement("td");
        const editButton = document.createElement("button");

        editButton.textContent = "Edit";
        editButton.classList.add("btn", "btn-info", "btn-sm");
        editButton.addEventListener("click", function () {
            openEditModal(user);
        });

        editCell.appendChild(editButton);
        row.appendChild(editCell);

        const deleteCell = document.createElement("td");
        const deleteButton = document.createElement("button");

        deleteButton.textContent = "Delete";
        deleteButton.classList.add("btn", "btn-danger", "btn-sm");
        deleteButton.addEventListener("click", function () {
            openDeleteModal(user);
        });

        deleteCell.appendChild(deleteButton);
        row.appendChild(deleteCell);

        tableBody.appendChild(row);
    }
}

function openCreateModal() {
    editingUserId = null;

    document.getElementById("user-modal-title")
        .textContent = "Add user";

    document.getElementById("user-form").reset();
    document.getElementById("password").required = true;
    document.getElementById("password-help").textContent =
        "Password is required when creating a user.";

    clearErrors();
    renderRoles([]);

    userModal.show();
}

function openEditModal(user) {
    editingUserId = user.id;

    document.getElementById("user-modal-title")
        .textContent = "Edit user";

    document.getElementById("name").value = user.name;
    document.getElementById("surname").value = user.surname;
    document.getElementById("email").value = user.email;
    document.getElementById("username").value = user.username;
    document.getElementById("password").value = "";
    document.getElementById("password").required = false;
    document.getElementById("password-help").textContent =
        "Leave empty to keep the current password.";

    clearErrors();
    renderRoles(user.roles);

    userModal.show();
}

function renderRoles(selectedRoleNames) {
    const select = document.getElementById("role-ids");
    select.innerHTML = "";

    for (const role of roles) {
        const option = document.createElement("option");

        option.value = role.id;
        option.textContent = role.name;
        option.selected = selectedRoleNames.includes(role.name);

        select.appendChild(option);
    }
}

async function saveUser(event) {
    event.preventDefault();

    const selectedRoles = Array.from(
        document.getElementById("role-ids").selectedOptions
    );

    const requestData = {
        name: document.getElementById("name").value,
        surname: document.getElementById("surname").value,
        email: document.getElementById("email").value,
        username: document.getElementById("username").value,
        password: document.getElementById("password").value,
        roleIds: selectedRoles.map(option => Number(option.value))
    };

    const url = editingUserId === null
        ? "/api/admin/users"
        : "/api/admin/users/" + editingUserId;

    const method = editingUserId === null
        ? "POST"
        : "PUT";

    const response = await fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json",
            [getCsrfHeaderName()]: getCsrfToken()
        },
        body: JSON.stringify(requestData)
    });

    if (!response.ok) {
        const errors = await response.json();
        showErrors(errors);
        return;
    }

    userModal.hide();
    await loadUsers();
}

function openDeleteModal(user) {
    deletingUserId = user.id;

    document.getElementById("delete-user-name")
        .textContent = user.username;

    deleteModal.show();
}

async function deleteUser() {
    const response = await fetch(
        "/api/admin/users/" + deletingUserId,
        {
            method: "DELETE",
             headers: {
                        [getCsrfHeaderName()]: getCsrfToken()
                        }
        }
    );

    if (!response.ok) {
        alert("Could not delete user");
        return;
    }

    deleteModal.hide();
    await loadUsers();
}

function clearErrors() {
    const errorBox = document.getElementById("form-errors");

    errorBox.textContent = "";
    errorBox.classList.add("d-none");
}

function showErrors(errors) {
    const errorBox = document.getElementById("form-errors");

    const messages = Object.values(errors);
    errorBox.textContent = messages.join(", ");

    errorBox.classList.remove("d-none");
}
