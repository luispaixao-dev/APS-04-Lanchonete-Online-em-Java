package org.example.lanchonete.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.lanchonete.dao.DaoCliente;
import org.example.lanchonete.model.Cliente;
import org.example.lanchonete.model.Endereco;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "cadastro", urlPatterns = {"/cadastro"})
public class Cadastro extends HttpServlet {

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder jsonBuilder = new StringBuilder();

        BufferedReader br = request.getReader(); // Removido o try-with-resources

        if (br != null) { // Verifique se o objeto br não está nulo
            String line;
            while ((line = br.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }

        if (!jsonBuilder.isEmpty()) {
            String json = jsonBuilder.toString();

            JSONObject dados = new JSONObject(json);

            Endereco endereco = criarEndereco(dados.getJSONObject("endereco"));
            Cliente cliente = criarCliente(dados.getJSONObject("usuario"), endereco);

            DaoCliente clienteDAO = new DaoCliente();
            clienteDAO.salvar(cliente);

            try (PrintWriter out = response.getWriter()) {
                out.println("Usuário Cadastrado!");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Dados Incompletos!");
            return;
        }

    }

    private Endereco criarEndereco(JSONObject enderecoJson) throws JSONException {
        Endereco endereco = new Endereco();
        endereco.setBairro(enderecoJson.getString("bairro"));
        endereco.setCidade(enderecoJson.getString("cidade"));
        endereco.setEstado(enderecoJson.getString("estado"));
        endereco.setComplemento(enderecoJson.getString("complemento"));
        endereco.setRua(enderecoJson.getString("rua"));
        endereco.setNumero(enderecoJson.getInt("numero"));
        return endereco;
    }

    public Cliente criarCliente(JSONObject usuarioJson, Endereco endereco) throws JSONException {
        Cliente cliente = new Cliente();
        cliente.setNome(usuarioJson.getString("nome"));
        cliente.setSobrenome(usuarioJson.getString("sobrenome"));
        cliente.setTelefone(usuarioJson.getString("telefone"));
        cliente.setUsuario(usuarioJson.getString("usuario"));
        cliente.setSenha(usuarioJson.getString("senha"));
        cliente.setFg_ativo(1);
        cliente.setEndereco(endereco);
        return cliente;
    }

//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        try {
//            processRequest(request, response);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }

}