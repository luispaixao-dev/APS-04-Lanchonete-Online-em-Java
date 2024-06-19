/**
 * Faz uma requisição assíncrona para um servlet.
 * @param {string} url - A URL do servlet.
 * @param {Function} callback - A função a ser chamada quando a resposta chegar.
 * @param {object|null} [data=null] - Os dados a serem enviados na requisição.
 */
function requisicao(url, callback, data = null) {
    try {
        const xhr = new XMLHttpRequest();
        xhr.open('POST', url, true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.onload = () => {
            if (xhr.status === 200) {
                callback(xhr.responseText);
            } else {
                console.error(`Request failed. Status code: ${xhr.status}`);
            }
        };
        xhr.onerror = () => {
            console.error('Network error occurred.');
        };
        xhr.send(data ? JSON.stringify(data) : null);
    } catch (error) {
        console.error('Error making request:', error);
    }
}

/**
 * Exibe a resposta da requisição no console.
 * @param {string} response - A resposta da requisição.
 */
function printResponse(response) {
    console.log(response);
}

/**
 * Exibe a resposta da requisição em um alerta.
 * @param {string} response - A resposta da requisição.
 */
function alertResponse(response) {
    alert(response);
}

/**
 * Verifica se um cookie com o nome especificado existe no documento.
 * @param {string} name - O nome do cookie.
 * @returns {boolean} - Verdadeiro se o cookie existe, falso caso contrário.
 */
function getCookie(name) {
    return document.cookie.split(';').some((cookie) => cookie.trim().startsWith(`${name}=`));
}