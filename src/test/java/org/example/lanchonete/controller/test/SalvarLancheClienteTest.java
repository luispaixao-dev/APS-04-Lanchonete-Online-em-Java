package org.example.lanchonete.controller.test;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.lanchonete.controller.SalvarLancheCliente;
import org.example.lanchonete.dao.DaoIngrediente;
import org.example.lanchonete.dao.DaoLanche;
import org.example.lanchonete.helper.ValidadorCookie;
import org.example.lanchonete.model.Ingrediente;
import org.example.lanchonete.model.Lanche;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SalvarLancheClienteTest {
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private DaoLanche mockDaoLanche;
    @Mock
    private DaoIngrediente mockDaoIngrediente;
    @Mock
    HttpServletRequest mockRequest;
    @InjectMocks
    private SalvarLancheCliente servlet;
    @Mock
    ValidadorCookie mockValidadorCookie;
    @Mock
    DaoLanche mockLancheDao;

    @Mock
    DaoIngrediente mockIngredienteDao;
    @Mock
    SalvarLancheCliente mockSalvarLancheCliente;

    SalvarLancheCliente salvarLancheCliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        salvarLancheCliente = new SalvarLancheCliente(mockLancheDao, mockIngredienteDao, mockValidadorCookie);
    }

    @Test
    void testValidarCookieCookieValido() {
        Cookie cookie = new Cookie("nomeCookie", "valorCookie");
        when(mockRequest.getCookies()).thenReturn(new Cookie[]{cookie});
        when(mockValidadorCookie.validar(new Cookie[]{cookie})).thenReturn(true);

        boolean resultado = salvarLancheCliente.validarCookie(mockRequest);

        assertTrue(resultado);
    }

    @Test
    void testValidarCookieCookieInvalido() {
        when(mockRequest.getCookies()).thenReturn(null);

        boolean resultado = salvarLancheCliente.validarCookie(mockRequest);

        assertFalse(resultado);
    }

    @Test
    void testValidarCookieCookieNulo() {
        Cookie cookie = new Cookie("nomeCookie", "valorCookie");
        when(mockRequest.getCookies()).thenReturn(new Cookie[]{cookie});
        when(mockValidadorCookie.validar(new Cookie[]{cookie})).thenReturn(false);

        boolean resultado = salvarLancheCliente.validarCookie(mockRequest);

        assertFalse(resultado);
    }

    @Test
    void testResponderComErro() throws Exception {
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);

        SalvarLancheCliente salvarLancheCliente = new SalvarLancheCliente(null, null, null);
        salvarLancheCliente.responderComErro(mockResponse);

        writer.flush();
        String resposta = stringWriter.toString().trim();

        assertEquals("erro", resposta);
    }

    @Test
    void testResponderComSucesso() throws Exception {
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);

        Lanche lanche = new Lanche();
        lanche.setNome("Lanche Teste");
        lanche.setValor_venda(12.5);

        SalvarLancheCliente salvarLancheCliente = new SalvarLancheCliente(null, null, null);
        salvarLancheCliente.responderComSucesso(mockResponse, lanche);

        writer.flush();
        String resposta = stringWriter.toString().trim();

        String nomeEncoded = "Lanche+Teste"; // URLEncoder.encode("Lanche Teste", StandardCharsets.UTF_8)
        String urlEsperada = "../carrinho/carrinho.html?nome=" + nomeEncoded + "&preco=" + 12.5;

        assertEquals(urlEsperada, resposta);
    }

    @Test
    void testValidarCookieComCookieValido () {
        Cookie cookieValido = new Cookie("meuCookie", "valorCookieValido");
        when(mockRequest.getCookies()).thenReturn(new Cookie[]{cookieValido});
        when(mockValidadorCookie.validar(any())).thenReturn(true);

        SalvarLancheCliente salvarLancheCliente = new SalvarLancheCliente(null, null, mockValidadorCookie);
        boolean resultado = salvarLancheCliente.validarCookie(mockRequest);

        assertTrue(resultado);
    }

    @Test
    void testValidarCookieComCookieInvalido () {
        when(mockRequest.getCookies()).thenReturn(null);

        SalvarLancheCliente salvarLancheCliente = new SalvarLancheCliente(null, null, mockValidadorCookie);
        boolean resultado = salvarLancheCliente.validarCookie(mockRequest);

        assertFalse(resultado);
    }

    @Test
    void testValidarCookieComCookieNulo () {
        Cookie cookieNulo = null;
        when(mockRequest.getCookies()).thenReturn(new Cookie[]{cookieNulo});
        when(mockValidadorCookie.validar(any())).thenReturn(false);

        SalvarLancheCliente salvarLancheCliente = new SalvarLancheCliente(null, null, mockValidadorCookie);
        boolean resultado = salvarLancheCliente.validarCookie(mockRequest);

        assertFalse(resultado);
    }
    @Test
    void testVincularIngredientes () throws Exception {
        Lanche lanche = new Lanche();
        lanche.setNome("Lanche Teste");

        JSONObject ingredientesJson = new JSONObject();
        ingredientesJson.put("queijo", 2);
        ingredientesJson.put("presunto", 1);

        Ingrediente queijo = new Ingrediente();
        queijo.setNome("queijo");
        queijo.setQuantidade(2);

        Ingrediente presunto = new Ingrediente();
        presunto.setNome("presunto");
        presunto.setQuantidade(1);

        when(mockLancheDao.pesquisaPorNome(lanche)).thenReturn(lanche);
        when(mockIngredienteDao.pesquisaPorNome(any())).thenReturn(queijo, presunto);

        SalvarLancheCliente salvarLancheCliente = new SalvarLancheCliente(mockLancheDao, mockIngredienteDao, null);
        salvarLancheCliente.vincularIngredientes(lanche, ingredientesJson);

        // Verifica se o método vincularIngrediente foi chamado no DAO com os ingredientes corretos
        verify(mockLancheDao, times(1)).vincularIngrediente(lanche, queijo);
        verify(mockLancheDao, times(1)).vincularIngrediente(lanche, presunto);
    }

    @Test
    void testCriarLanche() throws JSONException {
        JSONObject dados = new JSONObject();
        dados.put("nome", "Lanche Teste");
        dados.put("descricao", "Descrição Teste");

        SalvarLancheCliente salvarLancheCliente = new SalvarLancheCliente(null, null, null);
        Lanche lanche = salvarLancheCliente.criarLanche(dados, 10.0);

        assertEquals("Lanche Teste", lanche.getNome());
        assertEquals("Descrição Teste", lanche.getDescricao());
        assertEquals(10.0, lanche.getValor_venda());
    }

    @Test
    void testSalvarLanche() {
        Lanche lanche = new Lanche();
        lanche.setNome("Lanche Teste");

        SalvarLancheCliente salvarLancheCliente = new SalvarLancheCliente(mockLancheDao, null, null);
        salvarLancheCliente.salvarLanche(lanche);

        verify(mockLancheDao, times(1)).salvarCliente(lanche);
    }


    @Test
    void testResponderComErroComExcecao() throws Exception {
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(mockResponse.getWriter()).thenThrow(new IOException("Erro ao obter o writer"));

        SalvarLancheCliente salvarLancheCliente = new SalvarLancheCliente(null, null, null);

        assertThrows(IOException.class, () -> {
            salvarLancheCliente.responderComErro(mockResponse);
        });
    }
    @Test
    void testVincularIngredientesComIngredienteNaoEncontrado() throws Exception {
        Lanche lanche = new Lanche();
        lanche.setNome("Lanche Teste");

        JSONObject ingredientesJson = new JSONObject();
        ingredientesJson.put("queijo", 2);
        ingredientesJson.put("presunto", 1);

        when(mockLancheDao.pesquisaPorNome(lanche)).thenReturn(lanche);
        when(mockIngredienteDao.pesquisaPorNome(any())).thenThrow(new IngredienteNaoEncontradoException("Ingrediente não encontrado"));

        SalvarLancheCliente salvarLancheCliente = new SalvarLancheCliente(mockLancheDao, mockIngredienteDao, null);

        assertThrows(IngredienteNaoEncontradoException.class, () -> {
            salvarLancheCliente.vincularIngredientes(lanche, ingredientesJson);
        });

        // Verifica se o método vincularIngrediente não foi chamado no DAO
        verify(mockLancheDao, never()).vincularIngrediente(any(), any());
    }


    @Test
    void testCalcularPrecoLanche() throws JSONException, ServletException, IOException {
        // Ensure mockIngredienteDao is initialized and injected
        assertNotNull(mockIngredienteDao);

        JSONObject ingredientesJson = new JSONObject();
        ingredientesJson.put("queijo", 2);   // 2 unidades de queijo
        ingredientesJson.put("presunto", 1); // 1 unidade de presunto

        // Mockando o retorno do DAO para o ingrediente "queijo"
        Ingrediente queijo = new Ingrediente();
        queijo.setNome("queijo");
        queijo.setValor_venda(5.0); // Valor de venda do queijo
        when(mockIngredienteDao.pesquisaPorNome(any())).thenReturn(queijo);

        // Mockando o retorno do DAO para o ingrediente "presunto"
        Ingrediente presunto = new Ingrediente();
        presunto.setNome("presunto");
        presunto.setValor_venda(3.0); // Valor de venda do presunto
        when(mockIngredienteDao.pesquisaPorNome(any())).thenReturn(presunto);

        try {
            double precoTotal = salvarLancheCliente.calcularPrecoLanche(ingredientesJson);
            assertEquals(2 * 4.0 + 1, precoTotal); // Esperamos que o preço total seja calculado corretamente
        } catch (JSONException e) {
            // Se houver uma exceção JSONException, falharemos o teste
            e.printStackTrace();
            assertEquals(0.0, 1.0); // Forçando falha
        }
    }

    public class IngredienteNaoEncontradoException extends RuntimeException {

        public IngredienteNaoEncontradoException(String message) {
            super(message);
        }

        public IngredienteNaoEncontradoException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}