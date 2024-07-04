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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class SalvarLancheCliente extends HttpServlet {

    private final DaoLanche lancheDao;
    private final DaoIngrediente ingredienteDao;
    private final ValidadorCookie validadorCookie;

    public SalvarLancheCliente(DaoLanche lancheDao, DaoIngrediente ingredienteDao, ValidadorCookie validadorCookie) {
        this.lancheDao = lancheDao;
        this.ingredienteDao = ingredienteDao;
        this.validadorCookie = validadorCookie;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (!validarCookie(request)) {
            responderComErro(response);
            return;
        }

        String jsonStr = lerJson(request);
        if (jsonStr.isEmpty()) {
            responderComErro(response);
            return;
        }

        try {
            JSONObject dados = new JSONObject(jsonStr);
            JSONObject ingredientes = dados.getJSONObject("ingredientes");

            double precoDoLanche = calcularPrecoLanche(ingredientes);
            Lanche lanche = criarLanche(dados, precoDoLanche);
            salvarLanche(lanche);
            vincularIngredientes(lanche, ingredientes);
            responderComSucesso(response, lanche);
        } catch (JSONException e) {
            responderComErro(response);
        }
    }

    public boolean validarCookie(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();
            return validadorCookie.validar(cookies);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public String lerJson(HttpServletRequest request) throws IOException {
        StringBuilder json = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line);
            }
        }
        return json.toString();
    }

    public double calcularPrecoLanche(JSONObject ingredientes) throws JSONException {
        double precoTotal = 0.0;
        Iterator<String> keys = ingredientes.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Ingrediente ingredienteLanche = new Ingrediente();
            ingredienteLanche.setNome(key);
            Ingrediente ingredienteComID = ingredienteDao.pesquisaPorNome(ingredienteLanche);
            if (ingredienteComID != null) {
                precoTotal += ingredienteComID.getValor_venda() * ingredientes.getInt(key);
            }
        }
        return precoTotal;
    }

    public Lanche criarLanche(JSONObject dados, double precoDoLanche) throws JSONException {
        Lanche lanche = new Lanche();
        lanche.setNome(dados.getString("nome"));
        lanche.setDescricao(dados.getString("descricao"));
        lanche.setValor_venda(precoDoLanche);
        return lanche;
    }

    public void salvarLanche(Lanche lanche) {
        lancheDao.salvarCliente(lanche);
    }

    public void vincularIngredientes(Lanche lanche, JSONObject ingredientes) throws JSONException {
        Lanche lancheComID = lancheDao.pesquisaPorNome(lanche);
        Iterator<String> keys = ingredientes.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Ingrediente ingredienteLanche = new Ingrediente();
            ingredienteLanche.setNome(key);
            Ingrediente ingredienteComID = ingredienteDao.pesquisaPorNome(ingredienteLanche);
            if (ingredienteComID != null) {
                ingredienteComID.setQuantidade(ingredientes.getInt(key));
                lancheDao.vincularIngrediente(lancheComID, ingredienteComID);
            } else {
                System.out.println("Aviso: Ingrediente não encontrado para nome " + key);
            }
        }
    }

    public void responderComSucesso(HttpServletResponse response, Lanche lanche) throws IOException {
        String nomeEncoded = URLEncoder.encode(lanche.getNome(), StandardCharsets.UTF_8);
        String url = "../carrinho/carrinho.html?nome=" + nomeEncoded + "&preco=" + lanche.getValor_venda();
        try (PrintWriter out = response.getWriter()) {
            out.println(url);
        }
    }

    public void responderComErro(HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            out.println("erro");
        }
    }
}