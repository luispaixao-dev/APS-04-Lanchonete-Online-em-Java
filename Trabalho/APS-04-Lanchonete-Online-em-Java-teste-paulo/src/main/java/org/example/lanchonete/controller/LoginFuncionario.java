/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.lanchonete.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.lanchonete.dao.DaoFuncionario;
import org.example.lanchonete.dao.DaoToken;
import org.example.lanchonete.model.Funcionario;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.Instant;

/**
 *
 * @author kener_000
 */
public class LoginFuncionario extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException {
       //Seta o tipo de Conteudo que será recebido, nesse caso, um JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        //Para receber JSONs, é necessario utilizar esse Buffer para receber os dados,
        //Então tem que ser Feito assim:
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json;
        boolean resultado;
        
        
        //Aqui ele checa se os Dados não tão vazios, por motivos de vai que
        //Converte os dados do JSON para um Formato de Objeto que o Java consiga lidar
        json = br.readLine();
        JSONObject dados = new JSONObject(json);

        //Aqui, ele Instancia um objeto do Model Cliente, e Popula ele com os dados do JSON
        Funcionario funcionario = new Funcionario();
        funcionario.setUsuario(dados.getString("usuario"));
        funcionario.setSenha(dados.getString("senha"));

        /////////////////////////
        //E Para finalizar, salva no Banco usando o DAO dele

        DaoFuncionario funcionarioDAO = new DaoFuncionario();
        DaoToken tokenDAO = new DaoToken();
        resultado = funcionarioDAO.login(funcionario);

        if(resultado){
            Funcionario funcionarioCompleto = funcionarioDAO.pesquisaPorUsuario(funcionario);

            Cookie cookie = new Cookie("tokenFuncionario", funcionarioCompleto.getId()+"-"+Instant.now().toString());
            tokenDAO.salvar(cookie.getValue());
            cookie.setMaxAge(30*60);
            response.addCookie(cookie);
        }
        try (PrintWriter out = response.getWriter()) {
            
            //Aqui é onde a Resposta é mandada para o Cliente, dando um Feedback de que tudo deu certo.
            
            if(resultado){
                out.println("../painel/painel.html");
            } else {
                out.println("erro");
            }
            

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}