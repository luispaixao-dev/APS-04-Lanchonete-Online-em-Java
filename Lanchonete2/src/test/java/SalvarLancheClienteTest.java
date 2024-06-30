import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.lanchonete.controller.SalvarLancheCliente;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SalvarLancheClienteTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    void testProcessRequestValidRequest() throws Exception {
        // Configuração do BufferedReader simulado
        String jsonRequest = "{\"nome\":\"X-Burger\",\"descricao\":\"Delicioso lanche de hambúrguer\",\"ingredientes\":{\"pao\":1,\"queijo\":2}}";
        BufferedReader br = new BufferedReader(new StringReader(jsonRequest));
        when(request.getReader()).thenReturn(br);

        // Configuração do cookie válido
        Cookie cookie = new Cookie("nome_cookie", "valor_cookie");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        // Configuração do PrintWriter simulado
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Simular chamada ao método processRequest
        SalvarLancheCliente servlet = new SalvarLancheCliente();
        servlet.processRequestPublic(request, response); // Chamar novo método público

        // Verificar o resultado esperado
        writer.flush(); // Forçar a escrita
        String resultado = stringWriter.toString().trim();
        assertEquals("../carrinho/carrinho.html?nome=X-Burger&preco=12.5", resultado);
    }

    @Test
    void testProcessRequestInvalidCookie() throws Exception {
        // Configuração do BufferedReader simulado
        String jsonRequest = "{\"nome\":\"X-Burger\",\"descricao\":\"Delicioso lanche de hambúrguer\",\"ingredientes\":{\"pao\":1,\"queijo\":2}}";
        BufferedReader br = new BufferedReader(new StringReader(jsonRequest));
        when(request.getReader()).thenReturn(br);

        // Configuração do cookie inválido (null)
        when(request.getCookies()).thenReturn(null);

        // Configuração do PrintWriter simulado
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Simular chamada ao método processRequest
        SalvarLancheCliente servlet = new SalvarLancheCliente();
        servlet.processRequestPublic(request, response); // Chamar novo método público

        // Verificar o resultado esperado
        writer.flush(); // Forçar a escrita
        String resultado = stringWriter.toString().trim();
        assertEquals("erro", resultado);
    }
}