const enviarLogin = () => {

    let usuario = document.getElementById("loginInput").value;
    let senha = document.getElementById("senhaInput").value;

    if(usuario && senha){

        let dados = {};

        dados['usuario'] = usuario;
        dados['senha'] = senha;

        postRequest("../../login", resolver, JSON.stringify(dados));
        console.log("enviou")
    } else {
        alert("Digite as Informações!");
    }

};

function resolver(resposta) {
    console.log("entrou no resolver")
    let responseText = resposta.srcElement.responseText;
    console.log("print resposta", responseText);
    console.log(resposta.srcElement.srcElement)
    if (responseText !== "erro") {
        let queryString = window.location.search;
        let urlParams = new URLSearchParams(queryString);
        if (urlParams.has('Action')) {
            let action = urlParams.get('Action');
            if (action === "montarLanche") {
                window.location.replace("../montarLanche/montarLanche.html");
            } else {
                window.location.replace(responseText);
            }
        } else {
            window.location.replace(responseText);
        }
    } else {
        alert("Erro ao Logar! Tente novamente. Se Cadastre se não possuir uma conta!");
    }
}

function validarToken() {
    getRequest("../../validarToken", checkToken);
}

function checkToken(resposta) {
    let responseText = resposta.srcElement.responseText;
    console.log(responseText)
    if (responseText.includes("erro")) {
        console.log("Token Inválido");
    } else {
        redirectToPage();
    }
}

function redirectToPage() {
    let queryString = window.location.search;
    let urlParams = new URLSearchParams(queryString);
    if (urlParams.has('Action')) {
        let action = urlParams.get('Action');
        let targetPage = action === "montarLanche" ? "../montarLanche/montarLanche.html" : "../carrinho/carrinho.html";
        window.location.replace(targetPage);
    } else {
        window.location.replace("../carrinho/carrinho.html");
    }
}
const postRequest = (serveletPath, responseFunc, data = null) => {
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: data ? JSON.stringify(data) : null
    };

    fetch(serveletPath, options)
        .then(response => response.json())
        .then(responseFunc)
        .catch(error => {
            console.error('Erro na requisição POST:', error);
        });
};