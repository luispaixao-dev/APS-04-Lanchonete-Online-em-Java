package org.example.lanchonete.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.json.JSONException;
import org.json.JSONObject;
import org.example.lanchonete.dao.DaoIngrediente;
import org.example.lanchonete.dao.DaoLanche;
import org.example.lanchonete.helper.ValidadorCookie;
import org.example.lanchonete.model.Ingrediente;
import org.example.lanchonete.model.Lanche;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class SalvarLancheCliente extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException e) {
            throw new ServletException(e);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";

        // Validar Cookie
        boolean resultado = false;
        try {
            Cookie[] cookies = request.getCookies();
            ValidadorCookie validar = new ValidadorCookie();
            resultado = validar.validar(cookies);
        } catch (java.lang.NullPointerException e) {
        }

        if ((br != null) && resultado) {
            json = br.readLine();
            byte[] bytes = json.getBytes(StandardCharsets.ISO_8859_1);
            String jsonStr = new String(bytes, StandardCharsets.UTF_8);
            JSONObject dados = new JSONObject(jsonStr);
            JSONObject ingredientes = dados.getJSONObject("ingredientes");

            double precoDoLanche = 0.00;

            Lanche lanche = new Lanche();

            lanche.setNome(dados.getString("nome"));
            lanche.setDescricao(dados.getString("descricao"));

            DaoLanche lancheDao = new DaoLanche();
            DaoIngrediente ingredienteDao = new DaoIngrediente();

            Iterator<String> keys = ingredientes.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                Ingrediente ingredienteLanche = new Ingrediente();
                ingredienteLanche.setNome(key);

                Ingrediente ingredienteComID = ingredienteDao.pesquisaPorNome(ingredienteLanche);
                precoDoLanche += ingredienteComID.getValor_venda() * Double.valueOf(ingredientes.getInt(key));
            }

            lanche.setValor_venda(precoDoLanche);
            lancheDao.salvarCliente(lanche);

            Lanche lancheComID = lancheDao.pesquisaPorNome(lanche);

            keys = ingredientes.keys(); // Reiniciando o iterator

            while (keys.hasNext()) {
                String key = keys.next();
                Ingrediente ingredienteLanche = new Ingrediente();
                ingredienteLanche.setNome(key);

                Ingrediente ingredienteComID = ingredienteDao.pesquisaPorNome(ingredienteLanche);
                ingredienteComID.setQuantidade(ingredientes.getInt(key));
                lancheDao.vincularIngrediente(lancheComID, ingredienteComID);
            }

            try (PrintWriter out = response.getWriter()) {
                out.println("../carrinho/carrinho.html?nome=" + String.valueOf(lancheComID.getNome()) + "&preco=" + String.valueOf(lancheComID.getValor_venda()));
            }
        } else {
            try (PrintWriter out = response.getWriter()) {
                out.println("erro");
            }
        }
    }

    // Método público para ser usado nos testes unitários
    public void processRequestPublic(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
        processRequest(request, response);
    }
}
