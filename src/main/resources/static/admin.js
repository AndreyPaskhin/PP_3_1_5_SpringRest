const url = 'http://localhost:8080/api/admin';
const userUrl = 'http://localhost:8080/api/user';

document.addEventListener("DOMContentLoaded", async function () {
    const authUser = await getAuthUser();
    console.log(authUser);
    await fillHeaderText(authUser);
})

function getAllUsers() {
    fetch(url)
        .then(res => res.json())
        .then(data => {
            loadTable(data)
        })
}

function getAdminPage() {
    fetch(url).then(response => response.json()).then(user => {
        console.log(user);
        loadTable(user)
    })
}

function loadTable(listAllUsers) {
    let res = '';
    for (let user of listAllUsers) {
        res +=
            `<tr>
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td id=${'role' + user.id}>${user.roles.map(r => r.name).join(' ')}</td>
                <td>
                    <button class="btn btn-info" type="button"
                    data-bs-toggle="modal" data-bs-target="#editModal"
                    onclick="editModal(${user.id})">Edit</button></td>
                <td>
                    <button class="btn btn-danger" type="button"
                    data-bs-toggle="modal" data-bs-target="#deleteModal"
                    onclick="deleteModal(${user.id})">Delete</button></td>
            </tr>`
    }
    document.getElementById('tableBodyAdmin').innerHTML = res;
}

function getInformationAboutUser(user) {
    console.log(user);

    let result = '';
    result =
        `<tr>
    <td>${user.id}</td>
    <td>${user.name}</td>
    <td>${user.age}</td>
    <td>${user.email}</td>
    <td id=${'role' + user.id}>${user.roles.map(r => r.name).join(' ')}</td>
</tr>`
    document.getElementById('userTableBody').innerHTML = result;
}

function getUserPage() {
    fetch(userUrl).then(response => response.json()).then(user =>
        getInformationAboutUser(user))
}


getAdminPage();
getUserPage();


// Добавление пользователя
document.getElementById('newUserForm').addEventListener('submit', (e) => {
    e.preventDefault()
    // let role = document.getElementById('role_select')
    // let rolesAddUser = []
    // let rolesAddUserValue = ''
    // for (let i = 0; i < role.options.length; i++) {
    //     if (role.options[i].selected) {
    //         rolesAddUser.push({id: role.options[i].value, name: 'ROLE_' + role.options[i].innerHTML})
    //         rolesAddUserValue += role.options[i].innerHTML
    //     }
    // }

    let rolesAddUser = [];
    for (let i = 0; i < document.forms["newUserForm"].roles.options.length; i++) {
        if (document.forms["newUserForm"].roles.options[i].selected)
            rolesAddUser.push({
                id:document.forms["newUserForm"].roles.options[i].value,
                role:"ROLE " + document.forms["newUserForm"].roles.options[i].text
            });
    }


    fetch(url + '/save', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            name: document.getElementById('newName').value,
            age: document.getElementById('newAge').value,
            email: document.getElementById('newEmail').value,
            password: document.getElementById('newPassword').value,
            roles: rolesAddUser
        })
    })
        .then((response) => {
            if (response.ok) {
                getAllUsers()
                document.getElementById("all-users-tab").click()
            }
        })
})
function loadRolesNewUser() {
    let selectAdd = document.getElementById("role_select");
    selectAdd.innerHTML = "";
    fetch("http://localhost:8080/api/admin/roles")
        .then(res => res.json())
        .then(data => {
            data.forEach(role => {
                let option = document.createElement("option");
                option.value = role.id;
                option.text = role.name.toString().replace('ROLE ', '');
                selectAdd.appendChild(option);
            });
        })
        .catch(error => console.error(error));
}
window.addEventListener("load", loadRolesNewUser);


// Закрытие модального окна
function closeModal() {
    // document.getElementById("editClose").click()
    document.querySelectorAll(".btn-close").forEach((btn) => btn.click())
}


//Редактирование пользователя
function editModal(id) {
    fetch(url + '/' + id, {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    }).then(res => {
        res.json().then(u => {
            console.log(u);
            document.getElementById('editId').value = u.id;
            document.getElementById('editName').value = u.name;
            document.getElementById('editAge').value = u.age;
            document.getElementById('editEmail').value = u.email;
            document.getElementById('editPassword').value = "****";

        })
    });
}


async function editUser() {
    const form_ed = document.getElementById('modalEdit');
    if (!form_ed.checkValidity()) {
        // Показываем общее сообщение об ошибке, если форма невалидна
        form_ed.reportValidity();
        return;
    }

    let idValue = document.getElementById("editId").value;
    let nameValue = document.getElementById("editName").value;
    let ageValue = document.getElementById("editAge").value;
    let emailValue = document.getElementById("editEmail").value;
    let passwordValue = document.getElementById("editPassword").value;
    let listOfRole = [];
    // for (let i = 0; i < form_ed.roles.options.length; i++) {
    //     if (form_ed.roles.options[i].selected) {
    //         let tmp = {};
    //         tmp["id"] = form_ed.roles.options[i].value
    //         listOfRole.push(tmp);
    //         console.log(listOfRole);
    //     }
    // }
    for (let i = 0; i < document.forms["modalEdit"].roles.options.length; i++) {
        if (document.forms["modalEdit"].roles.options[i].selected)
            listOfRole.push({
                id: document.forms["modalEdit"].roles.options[i].value,
                role: "ROLE " + document.forms["modalEdit"].roles.options[i].text
            });
    }


    let user = {
        id: idValue,
        name: nameValue,
        age: ageValue,
        email: emailValue,
        password: passwordValue,
        roles: listOfRole
    }
    await fetch(url + '/update' + user.id, {
        method: "PATCH",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        },
        body: JSON.stringify(user)
    });
    closeModal()
    getAllUsers()
}
function loadRolesEdit() {
    let selectEdit = document.getElementById("editRole");
    selectEdit.innerHTML = "";

    fetch("http://localhost:8080/api/admin/roles")
        .then(res => res.json())
        .then(data => {
            data.forEach(role => {
                let option = document.createElement("option");
                option.value = role.id;
                option.text = role.name.toString().replace('ROLE ', '');
                selectEdit.appendChild(option);
            });
        })
        .catch(error => console.error(error));
}
window.addEventListener("load", loadRolesEdit);


// Удаление пользователя
function deleteModal(id) {
    fetch(url + '/' + id, {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    }).then(res => {
        res.json().then(u => {
            console.log(u);
            document.getElementById('deleteId').value = u.id;
            document.getElementById('deleteName').value = u.name;
            document.getElementById('deleteAge').value = u.age;
            document.getElementById('deleteEmail').value = u.email;
            document.getElementById("deleteRole").value = u.roles.map(r => r.name).join(", ");
        })
    });
}

async function deleteUser() {
    const id = document.getElementById("deleteId").value
    console.log(id)
    let urlDel = url + "/delete" + id;
    let method = {
        method: 'DELETE',
        headers: {
            "Content-Type": "application/json"
        }
    }

    fetch(urlDel, method).then(() => {
        closeModal()
        getAllUsers()
    })

}

async function fillHeaderText(authUser) {
    document.getElementById("header_text").innerText =
        `${authUser['username']} with roles: ${getUserRole(authUser['roles'])}`
}

function getUserRole(roles) {
    let result = "";
    for (const role of roles) {
        result += role["name"].replace('ROLE_', '') + " ";
    }
    console.log(result);
    return result;
}

async function getAuthUser() {
    const response = await fetch(`http://localhost:8080/api/user`);
    const authUser = await response.json();
    return authUser;
}