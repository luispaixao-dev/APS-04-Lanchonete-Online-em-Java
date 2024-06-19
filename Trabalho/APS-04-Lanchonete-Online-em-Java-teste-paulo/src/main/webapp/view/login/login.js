function enviarLogin() {
    const usuario = document.getElementById("loginInput").value.trim();
    const senha = document.getElementById("senhaInput").value.trim();

    if (usuario !== '' && senha !== '') {
        const dados = {
            usuario,
            senha
        };

        requisicao("../../login", resolver, JSON.stringify(dados));
    } else {
        alert("Digite as Informações!");
    }
}

function resolver(resposta) {
    if (resposta.srcElement.responseText.startsWith("erro")) {
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);

        if (urlParams.has('Action') && urlParams.get('Action').startsWith("montarLanche")) {
            window.location.href = "../montarLanche/montarLanche.html";
        } else {
            window.location.href = resposta.srcElement.responseText;
        }
    } else {
        alert("Erro ao Logar! Tente novamente. Se Cadastre se não possuir uma conta!");
    }
}

function validarToken() {
    const token = localStorage.getItem("token"); // supondo que o token esteja armazenado no localStorage
    if (token && token.trim() !== '') {
        requisicao("../../validarToken", check, token);
    } else {
        console.log("Token Inválido");
    }
}

function check(resposta) {
    if (resposta.srcElement.responseText.includes("erro")) {
        console.log("Token Inválido");
    } else {
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        if (urlParams.get('Action') && urlParams.get('Action').startsWith("montarLanche")) {
            window.location.href = "../montarLanche/montarLanche.html";
        } else {
            window.location.href = "../carrinho/carrinho.html";
        }
    }
}