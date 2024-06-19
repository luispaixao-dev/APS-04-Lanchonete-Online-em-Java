package Controllers;

import Model.Funcionario;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DAO.DaoFuncionario;
import org.example.DAO.DaoToken;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

public class LoginFuncionario extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder jsonBuilder = new StringBuilder();
        boolean loginSucesso = false;

        try (BufferedReader br = request.getReader()) {
            String line;
            while ((line = br.readLine()) != null) {
                jsonBuilder.append(line);
            }

            String json = jsonBuilder.toString();

            if (!json.isEmpty()) {
                JSONObject dados = new JSONObject(json);

                // Validar se os campos "usuario" e "senha" estão presentes
                if (dados.has("usuario") && dados.has("senha")) {
                    Funcionario funcionario = new Funcionario();
                    funcionario.setUsuario(dados.getString("usuario"));
                    funcionario.setSenha(dados.getString("senha"));

                    DaoFuncionario funcionarioDAO = new DaoFuncionario();
                    DaoToken tokenDAO = new DaoToken();

                    // Tentar fazer o login do funcionário
                    loginSucesso = funcionarioDAO.login(funcionario);

                    if (loginSucesso) {
                        Funcionario funcionarioCompleto = funcionarioDAO.pesquisaPorUsuario(funcionario);

                        // Gerar e salvar o token do funcionário
                        String token = funcionarioCompleto.getId() + "-" + Instant.now().toString();
                        tokenDAO.salvar(token);

                        // Criar e adicionar o cookie de autenticação à resposta
                        Cookie cookie = new Cookie("tokenFuncionario", token);
                        cookie.setMaxAge(30 * 60);
                        response.addCookie(cookie);
                    }
                }
            }
        } catch (Exception e) {
            // Tratar exceções de forma apropriada
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.println("Erro ao processar o login do funcionário.");
            }
            return;
        }

        try (PrintWriter out = response.getWriter()) {
            if (loginSucesso) {
                out.println("../painel/painel.html");
            } else {
                out.println("Erro no login");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}