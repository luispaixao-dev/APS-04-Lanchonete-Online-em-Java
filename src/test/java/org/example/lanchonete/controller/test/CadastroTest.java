package org.example.lanchonete.controller.test;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.lanchonete.controller.Cadastro;
import org.example.lanchonete.model.Cliente;
import org.example.lanchonete.model.Endereco;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CadastroTest {

    @Test
    public void testCriarCliente() {
        // Configuração
        Cadastro cadastro = new Cadastro();
        JSONObject usuarioJson = new JSONObject();
        usuarioJson.put("nome", "João");
        usuarioJson.put("sobrenome", "Silva");
        usuarioJson.put("telefone", "123456789");
        usuarioJson.put("usuario", "joao");
        usuarioJson.put("senha", "senha123");
        Endereco endereco = new Endereco();

        // Execução
        Cliente cliente = cadastro.criarCliente(usuarioJson, endereco);

        // Verificação
        assertEquals("João", cliente.getNome());
        assertEquals("Silva", cliente.getSobrenome());
        assertEquals("123456789", cliente.getTelefone());
        assertEquals("joao", cliente.getUsuario());
        assertEquals("senha123", cliente.getSenha());
        assertEquals(1, cliente.getFg_ativo());
        assertEquals(endereco, cliente.getEndereco());
    }

    @Test
    public void testProcessRequest() throws ServletException, IOException, JSONException {
        // Criação dos mocks
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        // Configuração do mock do request
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"endereco\": {\"bairro\": \"Centro\", \"cidade\": \"São Paulo\", \"estado\": \"SP\", \"complemento\": \"\", \"rua\": \"Rua A\", \"numero\": 123}, \"usuario\": {\"nome\": \"João\", \"sobrenome\": \"Silva\", \"telefone\": \"123456789\", \"usuario\": \"joao\", \"senha\": \"123456\"}}")));
        when(request.getSession()).thenReturn(session);

        // Configuração do mock do response
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Chamada do método a ser testado
        Cadastro servlet = new Cadastro();
        servlet.doPost(request, response);

        // Verificação do resultado
        writer.flush();
        String jsonResult = stringWriter.toString().trim();
        assertEquals("Usuário Cadastrado!", jsonResult);
    }

    @Test
    public void testProcessRequestErroDadosIncompletos() throws Exception {
        // Crie o objeto HttpServletResponse mockado
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

        // Crie o objeto Cadastro a ser testado
        Cadastro cadastro = new Cadastro();

        // Chame o método processRequest com dados incompletos
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getParameter("nome")).thenReturn(null); // Simule um valor nulo para o parâmetro "nome"
        when(httpServletRequest.getParameter("email")).thenReturn("teste@example.com"); // Simule um valor válido para o parâmetro "email"

        // Crie um objeto StringWriter para capturar a saída do método processRequest
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        // Configure o comportamento esperado para o método getWriter()
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        // Chame o método a ser testado
        cadastro.processRequest(httpServletRequest, httpServletResponse);

        // Verifique se o método getWriter() foi invocado
        verify(httpServletResponse).getWriter();

        // Verifique se os outros métodos foram invocados corretamente
        verify(httpServletResponse).setContentType("application/json");
        verify(httpServletResponse).setCharacterEncoding("UTF-8");
        verify(httpServletResponse).setStatus(400);

        // Verifique se a saída gerada está correta
        assertEquals("Dados Incompletos!", stringWriter.toString().trim());
    }



}